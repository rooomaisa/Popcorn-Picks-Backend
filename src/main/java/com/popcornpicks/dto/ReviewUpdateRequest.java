package com.popcornpicks.dto;

import jakarta.validation.constraints.NotNull;

public class ReviewUpdateRequest {
    @NotNull
    private Integer rating;

    @NotNull
    private String comment;

    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
