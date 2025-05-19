package com.popcornpicks.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "movieId is required")
    private Long movieId;

    @Min(value = 1, message = "rating must be at least 1")
    @Max(value = 5, message = "rating must be at most 5")
    private int rating;

    @NotBlank(message = "comment is required")
    private String comment;

    public ReviewRequest() { }

    public ReviewRequest(Long userId, Long movieId, int rating, String comment) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
