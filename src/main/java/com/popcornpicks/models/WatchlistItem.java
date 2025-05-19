package com.popcornpicks.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "watchlist_items",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "movie_id"},
                name = "uk_user_movie_watchlist"
        )
)
public class WatchlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(name = "added_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime addedAt;

    public WatchlistItem() { }

    public WatchlistItem(User user, Movie movie) {
        this.user = user;
        this.movie = movie;
    }

    // Getters

    public Long getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
    public Movie getMovie() {
        return movie;
    }
    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    // Setters

    // no setter for id or addedAt

    public void setUser(User user) {
        this.user = user;
    }
    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
