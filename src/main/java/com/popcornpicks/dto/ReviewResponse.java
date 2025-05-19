package com.popcornpicks.dto;

import java.time.LocalDateTime;

public class ReviewResponse {
    private Long id;
    private Long userId;
    private Long movieId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    public ReviewResponse() { }

    public ReviewResponse(
            Long id,
            Long userId,
            Long movieId,
            int rating,
            String comment,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
