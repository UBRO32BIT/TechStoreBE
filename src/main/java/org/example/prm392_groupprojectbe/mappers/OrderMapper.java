package org.example.prm392_groupprojectbe.mappers;

import org.example.prm392_groupprojectbe.dtos.orders.response.OrderResponseDTO;
import org.example.prm392_groupprojectbe.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public OrderResponseDTO toDto(Order order) {
        if (order == null) {
            return null;
        }

        return OrderResponseDTO.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    public Page<OrderResponseDTO> toDtoPage(Page<Order> orders) {
        return orders.map(this::toDto);
    }
}
