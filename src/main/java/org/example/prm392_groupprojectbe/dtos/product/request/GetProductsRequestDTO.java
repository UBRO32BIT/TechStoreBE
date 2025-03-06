package org.example.prm392_groupprojectbe.dtos.product.request;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class GetProductsRequestDTO {
    private Long categoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String search;
    private Integer minStock;
    private Integer maxStock;
    private int pageNumber;
    private int pageSize;
    private String sortBy;
    private Sort.Direction direction;
}

