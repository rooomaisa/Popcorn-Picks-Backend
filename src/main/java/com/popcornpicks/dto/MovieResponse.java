package com.popcornpicks.dto;

import java.time.LocalDateTime;
import java.util.List;

public class MovieResponse {
    private Long id;
    private String title;
    private Integer year;
    private String posterPath;
    private double averageRating;
    private List<String> genres;
    private LocalDateTime createdAt;

    public MovieResponse() { }

    public MovieResponse(
            Long id,
            String title,
            Integer year,
            String posterPath,
            double averageRating,
            List<String> genres,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.posterPath = posterPath;
        this.averageRating = averageRating;
        this.genres = genres;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }

    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
