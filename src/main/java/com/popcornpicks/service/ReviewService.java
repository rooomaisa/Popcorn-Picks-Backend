package com.popcornpicks.service;

import com.popcornpicks.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ReviewService {


    Review createReview(Long userId, Long movieId, int rating, String comment);


    Review updateReview(Long reviewId, int rating, String comment);


    void deleteReview(Long reviewId);

    Review getReviewById(Long id);


    Page<Review> getReviewsByMovie(Long movieId, Pageable pageable);


    Page<Review> getReviewsByUser(Long userId, Pageable pageable);
}
