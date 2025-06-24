package com.popcornpicks.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "reviews",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "movie_id"},
                name = "uk_user_movie_review"
        )
)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rating;  // 1–5

    @Column(length = 1000)
    private String comment;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Review() { }

    public Review(int rating, String comment, User user, Movie movie) {
        this.rating = rating;
        this.comment = comment;
        this.user = user;
        this.movie = movie;
    }



    public Long getId() {
        return id;
    }
    public int getRating() {
        return rating;
    }
    public String getComment() {
        return comment;
    }
    public User getUser() {
        return user;
    }
    public Movie getMovie() {
        return movie;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setId(Long id) {
        this.id = id;
    }



    public void setRating(int rating) {
        this.rating = rating;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
