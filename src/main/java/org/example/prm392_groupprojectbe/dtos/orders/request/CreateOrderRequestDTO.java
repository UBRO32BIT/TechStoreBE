package org.example.prm392_groupprojectbe.dtos.orders.request;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.example.prm392_groupprojectbe.dtos.cartitem.CartItemDTO;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequestDTO {
    @NotEmpty(message = "Order must contain at least one item")
    List<CartItemDTO> items;
}