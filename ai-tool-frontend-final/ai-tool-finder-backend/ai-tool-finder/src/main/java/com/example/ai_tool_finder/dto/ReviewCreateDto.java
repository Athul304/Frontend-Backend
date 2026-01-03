package com.example.ai_tool_finder.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewCreateDto {

    @NotBlank(message = "username must not be blank")
    private String username;

    @NotNull(message = "toolId must be provided")
    private Long toolId;

    @NotNull(message = "rating must be provided")
    @Min(value = 1, message = "rating must be at least 1")
    @Min(value = 1, message = "rating must be at least 1")
    @Max(value = 5, message = "rating must be at most 5")
    private Integer rating;

    private String comment;

    public ReviewCreateDto() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getToolId() { return toolId; }
    public void setToolId(Long toolId) { this.toolId = toolId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}

