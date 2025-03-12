package org.example.prm392_groupprojectbe.mappers;

import org.example.prm392_groupprojectbe.dtos.orderdetail.response.OrderDetailResponseDTO;
import org.example.prm392_groupprojectbe.entities.OrderDetail;

public class OrderDetailMapper {
    private OrderDetailMapper() {}

    public static OrderDetailResponseDTO toDTO(OrderDetail orderDetail) {
        if (orderDetail == null) {
            return null;
        }
        return OrderDetailResponseDTO.builder()
                .product(orderDetail.getProduct())
                .quantity(orderDetail.getQuantity())
                .price(orderDetail.getPrice())
                .build();
    }
}
