package org.example.prm392_groupprojectbe.dtos.orderdetail.response;

import lombok.*;
import org.example.prm392_groupprojectbe.entities.Product;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponseDTO {
    private Product product;
    private Integer quantity;
    private BigDecimal price;
}
