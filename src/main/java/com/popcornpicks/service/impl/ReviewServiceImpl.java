package com.popcornpicks.service.impl;

import com.popcornpicks.exceptions.DuplicateReviewException;
import com.popcornpicks.exceptions.ResourceNotFoundException;
import com.popcornpicks.models.Movie;
import com.popcornpicks.models.Review;
import com.popcornpicks.models.User;
import com.popcornpicks.repository.MovieRepository;
import com.popcornpicks.repository.ReviewRepository;
import com.popcornpicks.repository.UserRepository;
import com.popcornpicks.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            UserRepository userRepository,
            MovieRepository movieRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public Review createReview(Long userId, Long movieId, int rating, String comment) {
        // 1. Validate rating
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        // 2. Load user & movie
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + movieId));

        // 3. Enforce one review per user/movie
        if (reviewRepository.findByUserIdAndMovieId(userId, movieId).isPresent()) {
            throw new DuplicateReviewException(
                    "Review already exists for user " + userId + " and movie " + movieId
            );
        }

        // 4. Save review
        Review review = new Review(rating, comment, user, movie);
        Review saved = reviewRepository.save(review);

        // 5. Recalculate and persist averageRating
        recalcAverageRating(movieId);

        return saved;
    }

    @Override
    public Review updateReview(Long reviewId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Review existing = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + reviewId));

        existing.setRating(rating);
        existing.setComment(comment);
        Review updated = reviewRepository.save(existing);

        recalcAverageRating(existing.getMovie().getId());
        return updated;
    }

    @Override
    public void deleteReview(Long reviewId) {
        Review existing = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + reviewId));
        Long movieId = existing.getMovie().getId();

        reviewRepository.delete(existing);
        recalcAverageRating(movieId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Review> getReviewsByMovie(Long movieId, Pageable pageable) {
        if (!movieRepository.existsById(movieId)) {
            throw new ResourceNotFoundException("Movie not found with id " + movieId);
        }
        return reviewRepository.findByMovieId(movieId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Review> getReviewsByUser(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
        return reviewRepository.findByUserId(userId, pageable);
    }

    /** Helper to recalculate a movie’s averageRating */
    private void recalcAverageRating(Long movieId) {
        List<Review> reviews = reviewRepository.findByMovieId(movieId);
        double avg = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + movieId));
        movie.setAverageRating(avg);
        movieRepository.save(movie);
    }
}
