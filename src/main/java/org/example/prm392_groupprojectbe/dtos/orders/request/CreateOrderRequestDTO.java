package org.example.prm392_groupprojectbe.dtos.orders.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.prm392_groupprojectbe.dtos.cartitem.CartItemDTO;
import org.example.prm392_groupprojectbe.enums.PaymentMethod;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequestDTO {
    @NotEmpty(message = "Order must contain at least one item")
    private List<CartItemDTO> items;
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}