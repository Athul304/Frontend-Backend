package com.example.ai_tool_finder.service;

import com.example.ai_tool_finder.model.*;
import com.example.ai_tool_finder.repository.ReviewRepository;
import com.example.ai_tool_finder.repository.ToolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminReviewService {

    private final ReviewRepository reviewRepository;
    private final ToolRepository toolRepository;

    public AdminReviewService(ReviewRepository reviewRepository,
                              ToolRepository toolRepository) {
        this.reviewRepository = reviewRepository;
        this.toolRepository = toolRepository;
    }

    @Transactional
    public Review approveReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setStatus(ReviewStatus.APPROVED);
        Review saved = reviewRepository.save(review);

        updateAverageRating(saved.getTool().getId());
        return saved;
    }

    @Transactional
    public Review rejectReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setStatus(ReviewStatus.REJECTED);
        Review saved = reviewRepository.save(review);
        return saved;
    }

    private void updateAverageRating(Long toolId) {

        List<Review> approvedReviews =
                reviewRepository.findByToolIdAndStatus(toolId, ReviewStatus.APPROVED);

        double avgRating = approvedReviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        Tool tool = toolRepository.findById(toolId)
                .orElseThrow(() -> new RuntimeException("Tool not found"));

        tool.setAverageRating(avgRating);
        toolRepository.save(tool);
    }
}
