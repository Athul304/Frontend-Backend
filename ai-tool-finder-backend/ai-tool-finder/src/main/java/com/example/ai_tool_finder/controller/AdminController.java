package com.example.ai_tool_finder.controller;

import com.example.ai_tool_finder.dto.ReviewResponseDto;
import com.example.ai_tool_finder.dto.ReviewStatusUpdateDto;
import com.example.ai_tool_finder.model.Review;
import com.example.ai_tool_finder.model.ReviewStatus;
import com.example.ai_tool_finder.model.Role;
import com.example.ai_tool_finder.model.User;
import com.example.ai_tool_finder.repository.ReviewRepository;
import com.example.ai_tool_finder.service.AdminReviewService;
import com.example.ai_tool_finder.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final AdminReviewService adminReviewService;
    private final ReviewRepository reviewRepository;

    public AdminController(UserService userService, AdminReviewService adminReviewService, ReviewRepository reviewRepository) {
        this.userService = userService;
        this.adminReviewService = adminReviewService;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/test")
    public String adminTest() {
        User user = userService.getAdminUser();

        if (user.getRole() != Role.ADMIN) {
            return "Access denied";
        }
        return "Admin access granted";
    }

    @GetMapping("/reviews/pending")
    public List<ReviewResponseDto> getPendingReviews() {
        return reviewRepository.findAll().stream()
                .filter(r -> r.getStatus() == ReviewStatus.PENDING)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/reviews/{id}/approve")
    public ReviewResponseDto approveReview(@PathVariable Long id) {
        Review saved = adminReviewService.approveReview(id);
        return toDto(saved);
    }

    @PostMapping("/reviews/{id}/reject")
    public ReviewResponseDto rejectReview(@PathVariable Long id) {
        Review saved = adminReviewService.rejectReview(id);
        return toDto(saved);
    }

    @PutMapping("/reviews/{id}/status")
    public ReviewResponseDto updateReviewStatus(@PathVariable Long id, @Valid @RequestBody ReviewStatusUpdateDto dto) {
        ReviewStatus target = dto.getStatus();
        if (target == ReviewStatus.APPROVED) {
            Review saved = adminReviewService.approveReview(id);
            return toDto(saved);
        } else if (target == ReviewStatus.REJECTED) {
            Review saved = adminReviewService.rejectReview(id);
            return toDto(saved);
        } else {
            // For PENDING or other statuses, simply set and save via admin service by delegating to reject/approve logic
            // If needed, we could add a dedicated method to set arbitrary status. For now, throw.
            throw new IllegalArgumentException("Unsupported status transition: " + target);
        }
    }

    private ReviewResponseDto toDto(Review review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setStatus(review.getStatus() != null ? review.getStatus().name() : null);
        dto.setUsername(review.getUser() != null ? review.getUser().getUsername() : null);
        dto.setToolId(review.getTool() != null ? review.getTool().getId() : null);
        return dto;
    }
}
