package com.example.ai_tool_finder.model;

import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating;

    private String comment;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "tool_id")
    private Tool tool;

    public Review() {}

    public Review(int rating, String comment, ReviewStatus status, User user, Tool tool) {
        this.rating = rating;
        this.comment = comment;
        this.status = status;
        this.user = user;
        this.tool = tool;
    }

    public Long getId() { return id; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public ReviewStatus getStatus() { return status; }
    public void setStatus(ReviewStatus status) { this.status = status; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Tool getTool() { return tool; }
    public void setTool(Tool tool) { this.tool = tool; }
}
