package org.example.prm392_groupprojectbe.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.dtos.review.ReviewDTO;
import org.example.prm392_groupprojectbe.entities.Account;
import org.example.prm392_groupprojectbe.entities.Product;
import org.example.prm392_groupprojectbe.entities.Review;
import org.example.prm392_groupprojectbe.exceptions.AppException;
import org.example.prm392_groupprojectbe.exceptions.ErrorCode;
import org.example.prm392_groupprojectbe.repositories.AccountRepository;
import org.example.prm392_groupprojectbe.repositories.ProductRepository;
import org.example.prm392_groupprojectbe.repositories.ReviewRepository;
import org.example.prm392_groupprojectbe.services.ReviewService;
import org.example.prm392_groupprojectbe.utils.AccountUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    @Override
    public Review createReview(ReviewDTO reviewDTO) {
        Account user = AccountUtils.getCurrentAccount();
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        Product product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Review review = Review.builder()
                .user(user)
                .product(product)
                .rating(reviewDTO.getRating())
                .comment(reviewDTO.getComment())
                .build();

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }
}