package org.example.prm392_groupprojectbe.dtos.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private String comment;
    private int rating;
    private Long productId; // ID của sản phẩm mà đánh giá này thuộc về
}