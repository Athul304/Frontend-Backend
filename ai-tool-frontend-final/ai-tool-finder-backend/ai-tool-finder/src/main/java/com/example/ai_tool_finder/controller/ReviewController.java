package com.example.ai_tool_finder.controller;

import com.example.ai_tool_finder.dto.ReviewCreateDto;
import com.example.ai_tool_finder.dto.ReviewResponseDto;
import com.example.ai_tool_finder.model.Review;
import com.example.ai_tool_finder.model.Tool;
import com.example.ai_tool_finder.model.User;
import com.example.ai_tool_finder.repository.ReviewRepository;
import com.example.ai_tool_finder.repository.ToolRepository;
import com.example.ai_tool_finder.repository.UserRepository;
import com.example.ai_tool_finder.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserRepository userRepository;
    private final ToolRepository toolRepository;
    private final ReviewRepository reviewRepository;

    public ReviewController(ReviewService reviewService, UserRepository userRepository, ToolRepository toolRepository, ReviewRepository reviewRepository) {
        this.reviewService = reviewService;
        this.userRepository = userRepository;
        this.toolRepository = toolRepository;
        this.reviewRepository = reviewRepository;
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@Valid @RequestBody ReviewCreateDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        Tool tool = toolRepository.findById(dto.getToolId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tool not found"));

        Review review = reviewService.addReview(user, tool, dto.getRating(), dto.getComment());

        ReviewResponseDto resp = toDto(review);

        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    @GetMapping
    public List<ReviewResponseDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ReviewResponseDto getReviewById(@PathVariable Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        return toDto(review);
    }

    @GetMapping("/tool/{toolId}")
    public List<ReviewResponseDto> getReviewsByTool(@PathVariable Long toolId) {
        return reviewRepository.findAll().stream()
                .filter(r -> r.getTool() != null && r.getTool().getId() != null && r.getTool().getId().equals(toolId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ReviewResponseDto toDto(Review review) {
        ReviewResponseDto resp = new ReviewResponseDto();
        resp.setId(review.getId());
        resp.setRating(review.getRating());
        resp.setComment(review.getComment());
        resp.setStatus(review.getStatus() != null ? review.getStatus().name() : null);
        resp.setUsername(review.getUser() != null ? review.getUser().getUsername() : null);
        resp.setToolId(review.getTool() != null ? review.getTool().getId() : null);
        return resp;
    }
}
