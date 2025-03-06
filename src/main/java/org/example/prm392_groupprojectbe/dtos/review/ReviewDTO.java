package org.example.prm392_groupprojectbe.dtos.review;

import lombok.Getter;
import lombok.Setter;
import org.example.prm392_groupprojectbe.enums.Rating;

@Getter
@Setter
public class ReviewDTO {
    private String comment;
    private Rating rating;
    private Long productId; // ID của sản phẩm mà đánh giá này thuộc về
}