package com.example.ai_tool_finder.repository;

import com.example.ai_tool_finder.model.Review;
import com.example.ai_tool_finder.model.ReviewStatus;
import com.example.ai_tool_finder.model.Tool;
import com.example.ai_tool_finder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    //For average rating calculation (admin logic)
    List<Review> findByToolIdAndStatus(Long toolId, ReviewStatus status);

    // For enforcing ONE review per user per tool
    Optional<Review> findByUserAndTool(User user, Tool tool);
}
