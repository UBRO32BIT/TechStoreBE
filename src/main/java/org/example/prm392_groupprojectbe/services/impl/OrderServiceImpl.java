package org.example.prm392_groupprojectbe.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.dtos.cartitem.CartItemDTO;
import org.example.prm392_groupprojectbe.dtos.orders.request.CreateOrderRequestDTO;
import org.example.prm392_groupprojectbe.dtos.orders.request.GetOrdersRequestDTO;
import org.example.prm392_groupprojectbe.dtos.orders.response.OrderResponseDTO;
import org.example.prm392_groupprojectbe.dtos.payment.response.PaymentResponseDTO;
import org.example.prm392_groupprojectbe.entities.Account;
import org.example.prm392_groupprojectbe.entities.Order;
import org.example.prm392_groupprojectbe.entities.OrderDetail;
import org.example.prm392_groupprojectbe.entities.Product;
import org.example.prm392_groupprojectbe.enums.OrderStatus;
import org.example.prm392_groupprojectbe.enums.PaymentMethod;
import org.example.prm392_groupprojectbe.exceptions.AppException;
import org.example.prm392_groupprojectbe.exceptions.ErrorCode;
import org.example.prm392_groupprojectbe.mappers.OrderMapper;
import org.example.prm392_groupprojectbe.repositories.AccountRepository;
import org.example.prm392_groupprojectbe.repositories.OrderDetailRepository;
import org.example.prm392_groupprojectbe.repositories.OrderRepository;
import org.example.prm392_groupprojectbe.repositories.ProductRepository;
import org.example.prm392_groupprojectbe.services.OrderService;
import org.example.prm392_groupprojectbe.services.PaymentService;
import org.example.prm392_groupprojectbe.services.ProductService;
import org.example.prm392_groupprojectbe.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final ProductService productService;
    private PaymentService paymentService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    @Lazy
    @Autowired
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    @Transactional
    public OrderResponseDTO createOrder(CreateOrderRequestDTO requestDTO) {
        if (requestDTO.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        Account user = AccountUtils.getCurrentAccount();
        if (user == null) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        List<OrderDetail> orderDetails = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItemDTO item : requestDTO.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            if (product.getStock() < item.getQuantity()) {
                throw new AppException(ErrorCode.OUT_OF_STOCK);
            }

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);

            OrderDetail orderDetail = OrderDetail.builder()
                    .product(product)
                    .quantity(item.getQuantity())
                    .price(product.getPrice())
                    .build();

            orderDetails.add(orderDetail);
        }

        Order order = Order.builder()
                .user(user)
                .totalPrice(totalPrice)
                .status(
                        PaymentMethod.CASH.equals(requestDTO.getPaymentMethod())
                                ? OrderStatus.PREPARING
                                : OrderStatus.PENDING
                )
                .build();

        Order savedOrder = orderRepository.save(order);
        for (OrderDetail detail : orderDetails) {
            detail.setOrder(savedOrder);
        }
        List<OrderDetail> savedDetails = orderDetailRepository.saveAll(orderDetails);

        PaymentResponseDTO payment = paymentService.createPayment(
                order,
                orderDetails,
                requestDTO.getPaymentMethod()
        );

        if (PaymentMethod.CASH.equals(requestDTO.getPaymentMethod())) {
            savedDetails.forEach(orderDetail -> productService.updateStockAfterPayment(orderDetail.getProduct(), orderDetail.getQuantity()));
        }

        return orderMapper.toDto(savedOrder, orderDetails, payment);
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.toDto(order, order.getOrderDetails(), null);
    }

    @Override
    public Page<OrderResponseDTO> getAllOrders(GetOrdersRequestDTO requestDTO) {
        List<String> allowedSortFields = List.of("id", "totalPrice", "status", "createdAt", "updatedAt");
        if (!allowedSortFields.contains(requestDTO.getSortBy())) {
            throw new AppException(ErrorCode.INVALID_SORT_FIELD);
        }

        Sort sort = Sort.by(requestDTO.getDirection(), requestDTO.getSortBy());
        Pageable pageable = PageRequest.of(requestDTO.getPageNumber(), requestDTO.getPageSize(), sort);
        Page<Order> orders = orderRepository.findByParameters(
                requestDTO.getStartDate(),
                requestDTO.getEndDate(),
                requestDTO.getStatus(),
                pageable
        );
        return orderMapper.toDtoPage(orders);
    }

    @Override
    public List<OrderResponseDTO> getOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(order -> orderMapper.toDto(order, order.getOrderDetails(), null)).toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersByCurrentUser() {
        Account account = AccountUtils.getCurrentAccount();
        if (account == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        List<Order> result = orderRepository.findByUserIdAndIsDeletedFalse(account.getId());
        return result.stream()
                .map(order -> orderMapper.toDto(order, order.getOrderDetails(), null))
                .toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersByUserId(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<Order> result = orderRepository.findByUserIdAndIsDeletedFalse(account.getId());
        return result.stream()
                .map(order -> orderMapper.toDto(order, order.getOrderDetails(), null))
                .toList();
    }

    @Override
    public void handlePaymentCompletion(Order order) {
        order.setStatus(OrderStatus.PREPARING);
        orderRepository.save(order);
        List<OrderDetail> orderDetails = order.getOrderDetails();
        orderDetails.forEach(
                orderDetail -> productService.updateStockAfterPayment(
                        orderDetail.getProduct(),
                        orderDetail.getQuantity()
                )
        );
    }

    @Override
    public void handleRefund(Order order) {
        List<OrderDetail> orderDetails = order.getOrderDetails();
        orderDetails.forEach(
                orderDetail -> productService.updateStockAfterOrderFailure(
                        orderDetail.getProduct(),
                        orderDetail.getQuantity()
                )
        );
    }

    @Override
    public OrderResponseDTO updateOrder(Long id, Order updatedOrder) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        existingOrder.setTotalPrice(updatedOrder.getTotalPrice());
        existingOrder.setStatus(updatedOrder.getStatus());
        Order savedOrder = orderRepository.save(existingOrder);
        return orderMapper.toDto(savedOrder, savedOrder.getOrderDetails(), null);
    }
}

