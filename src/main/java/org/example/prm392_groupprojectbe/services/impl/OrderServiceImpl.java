package org.example.prm392_groupprojectbe.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.dtos.orders.request.GetOrdersRequestDTO;
import org.example.prm392_groupprojectbe.dtos.orders.response.OrderResponseDTO;
import org.example.prm392_groupprojectbe.entities.Order;
import org.example.prm392_groupprojectbe.exceptions.AppException;
import org.example.prm392_groupprojectbe.exceptions.ErrorCode;
import org.example.prm392_groupprojectbe.mappers.OrderMapper;
import org.example.prm392_groupprojectbe.repositories.OrderRepository;
import org.example.prm392_groupprojectbe.services.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDTO createOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.toDto(order);
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
        return orders.stream().map(orderMapper::toDto).toList();
    }

    @Override
    public OrderResponseDTO updateOrder(Long id, Order updatedOrder) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        existingOrder.setTotalPrice(updatedOrder.getTotalPrice());
        existingOrder.setStatus(updatedOrder.getStatus());
        Order savedOrder = orderRepository.save(existingOrder);
        return orderMapper.toDto(savedOrder);
    }
}

