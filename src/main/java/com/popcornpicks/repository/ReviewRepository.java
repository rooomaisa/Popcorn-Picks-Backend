package com.popcornpicks.repository;

import com.popcornpicks.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Find the review a given user wrote for a given movie (to enforce one‐per‐user rule).
     */
    Optional<Review> findByUserIdAndMovieId(Long userId, Long movieId);

    /**
     * List all reviews for a movie (e.g. to show them on the movie detail page).
     */
    Page<Review> findByMovieId(Long movieId, Pageable pageable);

    /**
     * List all reviews written by a user (e.g. in their profile).
     */
    Page<Review> findByUserId(Long userId, Pageable pageable);

    /**
     * Convenience: fetch all reviews for a movie so we can recalculate its averageRating.
     */
    List<Review> findByMovieId(Long movieId);
}
