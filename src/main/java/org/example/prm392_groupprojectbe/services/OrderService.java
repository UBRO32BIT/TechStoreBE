package org.example.prm392_groupprojectbe.services;

import org.example.prm392_groupprojectbe.dtos.orders.request.GetOrdersRequestDTO;
import org.example.prm392_groupprojectbe.dtos.orders.response.OrderResponseDTO;
import org.example.prm392_groupprojectbe.entities.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder(Order order);

    OrderResponseDTO getOrderById(Long id);

    Page<OrderResponseDTO> getAllOrders(GetOrdersRequestDTO requestDTO);

    List<OrderResponseDTO> getOrders();

    OrderResponseDTO updateOrder(Long id, Order updatedOrder);
}
