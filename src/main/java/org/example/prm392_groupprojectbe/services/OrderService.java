package org.example.prm392_groupprojectbe.services;

import org.example.prm392_groupprojectbe.dtos.orders.request.CreateOrderRequestDTO;
import org.example.prm392_groupprojectbe.dtos.orders.request.GetOrdersRequestDTO;
import org.example.prm392_groupprojectbe.dtos.orders.response.OrderResponseDTO;
import org.example.prm392_groupprojectbe.entities.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder(CreateOrderRequestDTO requestDTO);

    OrderResponseDTO getOrderById(Long id);

    Page<OrderResponseDTO> getAllOrders(GetOrdersRequestDTO requestDTO);

    List<OrderResponseDTO> getOrders();

    List<OrderResponseDTO> getOrdersByCurrentUser();

    OrderResponseDTO updateOrder(Long id, Order updatedOrder);
}
