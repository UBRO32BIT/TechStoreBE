package org.example.prm392_groupprojectbe.services;

import org.example.prm392_groupprojectbe.dtos.ReviewDTO;
import org.example.prm392_groupprojectbe.entities.Review;

import java.util.List;

public interface ReviewService {
    Review createReview(Long userId, ReviewDTO reviewDTO);
    List<Review> getReviewsByProductId(Long productId);
}