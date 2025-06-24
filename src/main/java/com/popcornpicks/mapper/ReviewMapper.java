package com.popcornpicks.mapper;

import com.popcornpicks.dto.ReviewRequest;
import com.popcornpicks.dto.ReviewResponse;
import com.popcornpicks.models.Movie;
import com.popcornpicks.models.Review;
import com.popcornpicks.models.User;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {


    public Review toEntity(ReviewRequest req, User user, Movie movie) {
        Review review = new Review();
        review.setUser(user);
        review.setMovie(movie);
        review.setRating(req.getRating());
        review.setComment(req.getComment());
        return review;
    }


    public ReviewResponse toDto(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getUser().getId(),
                review.getMovie().getId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}
