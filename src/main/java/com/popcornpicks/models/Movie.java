package com.popcornpicks.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private int year;

    private String posterPath;

    private double averageRating;

    @ElementCollection
    @CollectionTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id")
    )
    @Column(name = "genre")
    private List<String> genres = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Movie() { }

    public Movie(String title, int year, String posterPath, List<String> genres) {
        this.title = title;
        this.year = year;
        this.posterPath = posterPath;
        this.genres = genres;
        this.averageRating = 0.0;

    }



    public Long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public int getYear() {
        return year;
    }
    public String getPosterPath() {
        return posterPath;
    }
    public double getAverageRating() {
        return averageRating;
    }
    public List<String> getGenres() {
        return genres;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setId(Long id) {
        this.id = id;
    }



    public void setTitle(String title) {
        this.title = title;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }


}
