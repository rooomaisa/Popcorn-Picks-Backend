package com.popcornpicks.dto;

import java.time.LocalDateTime;

public class WatchlistResponse {
    private Long id;
    private Long movieId;
    private String title;        // optional, for convenience
    private LocalDateTime addedAt;

    public WatchlistResponse() {
    }

    public WatchlistResponse(Long id, Long movieId, String title, LocalDateTime addedAt) {
        this.id = id;
        this.movieId = movieId;
        this.title = title;
        this.addedAt = addedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}
