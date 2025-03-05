package org.example.prm392_groupprojectbe.dtos.orders.request;

import lombok.*;
import org.example.prm392_groupprojectbe.enums.OrderStatus;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetOrdersRequestDTO {
    private Integer pageSize;
    private Integer pageNumber;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private OrderStatus status;
    private String sortBy;
    private Sort.Direction direction;
}
