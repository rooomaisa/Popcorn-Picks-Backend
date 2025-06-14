package com.popcornpicks.service;

import com.popcornpicks.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ReviewService {

    /** Create a new review, enforce one-per-user-per-movie, and update movie’s averageRating */
    Review createReview(Long userId, Long movieId, int rating, String comment);

    /** Update an existing review’s rating/comment and recalculate averageRating */
    Review updateReview(Long reviewId, int rating, String comment);

    /** Delete a review and recalculate averageRating */
    void deleteReview(Long reviewId);

    Review getReviewById(Long id);

    /** List reviews for a specific movie */
    Page<Review> getReviewsByMovie(Long movieId, Pageable pageable);

    /** List reviews written by a specific user */
    Page<Review> getReviewsByUser(Long userId, Pageable pageable);
}
