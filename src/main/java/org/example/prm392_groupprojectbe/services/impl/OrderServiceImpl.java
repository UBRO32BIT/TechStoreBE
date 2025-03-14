package org.example.prm392_groupprojectbe.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.constants.ZaloPayConstants;
import org.example.prm392_groupprojectbe.dtos.cartitem.CartItemDTO;
import org.example.prm392_groupprojectbe.dtos.orders.request.CreateOrderRequestDTO;
import org.example.prm392_groupprojectbe.dtos.orders.request.GetOrdersRequestDTO;
import org.example.prm392_groupprojectbe.dtos.orders.response.OrderResponseDTO;
import org.example.prm392_groupprojectbe.dtos.zalopay.ZaloPayCreateOrderRequestDTO;
import org.example.prm392_groupprojectbe.dtos.zalopay.ZaloPayOrderResponseDTO;
import org.example.prm392_groupprojectbe.entities.Account;
import org.example.prm392_groupprojectbe.entities.Order;
import org.example.prm392_groupprojectbe.entities.OrderDetail;
import org.example.prm392_groupprojectbe.entities.Product;
import org.example.prm392_groupprojectbe.enums.OrderStatus;
import org.example.prm392_groupprojectbe.exceptions.AppException;
import org.example.prm392_groupprojectbe.exceptions.ErrorCode;
import org.example.prm392_groupprojectbe.mappers.OrderMapper;
import org.example.prm392_groupprojectbe.proxy.ZaloPayProxy;
import org.example.prm392_groupprojectbe.repositories.AccountRepository;
import org.example.prm392_groupprojectbe.repositories.OrderDetailRepository;
import org.example.prm392_groupprojectbe.repositories.OrderRepository;
import org.example.prm392_groupprojectbe.repositories.ProductRepository;
import org.example.prm392_groupprojectbe.services.OrderService;
import org.example.prm392_groupprojectbe.utils.AccountUtils;
import org.example.prm392_groupprojectbe.utils.DateUtil;
import org.example.prm392_groupprojectbe.utils.HMACUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final ZaloPayProxy zaloPayProxy;
    private final ObjectMapper objectMapper;
    private final AccountRepository accountRepository;

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
                .status(OrderStatus.PENDING)
                .build();

        Order savedOrder = orderRepository.save(order);
        for (OrderDetail detail : orderDetails) {
            detail.setOrder(savedOrder);
        }
        orderDetailRepository.saveAll(orderDetails);

        //CREATE PAYMENT CHECKOUT URL
        ZaloPayOrderResponseDTO paymentDetail = this.createPaymentUrl(user, order, orderDetails);

        return orderMapper.toDto(savedOrder, orderDetails, paymentDetail);
    }

    private ZaloPayOrderResponseDTO createPaymentUrl(Account account, Order order, List<OrderDetail> orderDetails) {
        try {
            ZaloPayCreateOrderRequestDTO requestDTO = ZaloPayCreateOrderRequestDTO.builder()
                    .appId(ZaloPayConstants.APP_ID)
                    .appTransId(DateUtil.getCurrentTimeString("yyMMdd") + "_" + new Date().getTime())
                    .appTime(System.currentTimeMillis())
                    .appUser(String.valueOf(account.getId()))
                    .amount(order.getTotalPrice().longValue())
                    .item("[{}]")
                    .bankCode(ZaloPayConstants.BANK_CODE)
                    .embedData("{}")
                    .description("Payment for order #" + order.getId())
                    .callbackUrl("")
                    .build();

            String combinedData = requestDTO.getAppId() + "|"
                    + requestDTO.getAppTransId() + "|"
                    + requestDTO.getAppUser() + "|"
                    + requestDTO.getAmount() + "|"
                    + requestDTO.getAppTime() +"|"
                    + requestDTO.getEmbedData() +"|"
                    + requestDTO.getItem();

            requestDTO.setMac(HMACUtil.HMacHexStringEncode(
                    HMACUtil.HMACSHA256,
                    ZaloPayConstants.KEY1,
                    combinedData
            ));
            ZaloPayOrderResponseDTO paymentDetail = zaloPayProxy.createOrder(requestDTO);
            if (paymentDetail.getReturnCode() != 1) {
                throw new RuntimeException(paymentDetail.getSubReturnMessage());
            }
            paymentDetail.setAppId(requestDTO.getAppId());

            return paymentDetail;
        }
        catch (FeignException ex) {
            throw ex;
        }
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
    public OrderResponseDTO updateOrder(Long id, Order updatedOrder) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        existingOrder.setTotalPrice(updatedOrder.getTotalPrice());
        existingOrder.setStatus(updatedOrder.getStatus());
        Order savedOrder = orderRepository.save(existingOrder);
        return orderMapper.toDto(savedOrder, savedOrder.getOrderDetails(), null);
    }
}

