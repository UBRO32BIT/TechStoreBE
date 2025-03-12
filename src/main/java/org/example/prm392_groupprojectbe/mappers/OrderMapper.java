package org.example.prm392_groupprojectbe.mappers;

import org.example.prm392_groupprojectbe.dtos.orders.response.OrderResponseDTO;
import org.example.prm392_groupprojectbe.dtos.zalopay.ZaloPayOrderResponseDTO;
import org.example.prm392_groupprojectbe.entities.Order;
import org.example.prm392_groupprojectbe.entities.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {
    public OrderResponseDTO toDto(Order order, List<OrderDetail> orderDetail, ZaloPayOrderResponseDTO zalopay) {
        if (order == null) {
            return null;
        }

        return OrderResponseDTO.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .totalPrice(order.getTotalPrice())
                .orderDetail(
                        orderDetail.stream()
                                .map(OrderDetailMapper::toDTO)
                                .toList()
                )
                .zalopay(zalopay)
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    public Page<OrderResponseDTO> toDtoPage(Page<Order> orders) {
        return orders.map(order -> this.toDto(order, null, null));
    }
}
