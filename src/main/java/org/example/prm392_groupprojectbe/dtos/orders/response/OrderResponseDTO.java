package org.example.prm392_groupprojectbe.dtos.orders.response;

import lombok.*;
import org.example.prm392_groupprojectbe.dtos.orderdetail.response.OrderDetailResponseDTO;
import org.example.prm392_groupprojectbe.dtos.payment.response.PaymentResponseDTO;
import org.example.prm392_groupprojectbe.dtos.zalopay.ZaloPayOrderResponseDTO;
import org.example.prm392_groupprojectbe.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private Long userId;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private List<OrderDetailResponseDTO> orderDetail;
    private PaymentResponseDTO payment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
