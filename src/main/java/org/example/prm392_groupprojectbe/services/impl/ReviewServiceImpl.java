package org.example.prm392_groupprojectbe.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.dtos.ReviewDTO;
import org.example.prm392_groupprojectbe.entities.Account;
import org.example.prm392_groupprojectbe.entities.Product;
import org.example.prm392_groupprojectbe.entities.Review;
import org.example.prm392_groupprojectbe.repositories.AccountRepository;
import org.example.prm392_groupprojectbe.repositories.ProductRepository;
import org.example.prm392_groupprojectbe.repositories.ReviewRepository;
import org.example.prm392_groupprojectbe.services.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;

    @Override
    public Review createReview(Long userId, ReviewDTO reviewDTO) {
        Account user = accountRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User  not found"));

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