package com.popcornpicks.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public class MovieRequest {

    @NotBlank(message = "title is required")
    private String title;

    @NotNull(message = "year is required")
    @Positive(message = "year must be positive")
    private Integer year;

    @NotBlank(message = "posterPath is required")
    private String posterPath;

    @NotNull(message = "genres list is required")
    private List<@NotBlank(message = "genre cannot be blank") String> genres;

    public MovieRequest() { }

    public MovieRequest(String title, Integer year, String posterPath, List<String> genres) {
        this.title = title;
        this.year = year;
        this.posterPath = posterPath;
        this.genres = genres;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }
}
