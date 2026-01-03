package com.example.ai_tool_finder.service;

import com.example.ai_tool_finder.model.*;
import com.example.ai_tool_finder.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review addReview(User user, Tool tool, int rating, String comment) {

        reviewRepository.findByUserAndTool(user, tool)
                .ifPresent(r -> {
                    throw new RuntimeException("User already reviewed this tool");
                });

        Review review = new Review();
        review.setUser(user);
        review.setTool(tool);
        review.setRating(rating);
        review.setComment(comment);
        review.setStatus(ReviewStatus.PENDING);

        return reviewRepository.save(review);
    }
}
