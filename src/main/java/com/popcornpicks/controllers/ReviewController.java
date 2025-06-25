package com.popcornpicks.controllers;

import com.popcornpicks.dto.ReviewRequest;
import com.popcornpicks.dto.ReviewUpdateRequest;
import com.popcornpicks.dto.ReviewResponse;
import com.popcornpicks.mapper.ReviewMapper;
import com.popcornpicks.models.Movie;
import com.popcornpicks.models.Review;
import com.popcornpicks.models.User;
import com.popcornpicks.service.MovieService;
import com.popcornpicks.service.ReviewService;
import com.popcornpicks.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final MovieService movieService;
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewController(
            ReviewService reviewService,
            UserService userService,
            MovieService movieService,
            ReviewMapper reviewMapper
    ) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.movieService = movieService;
        this.reviewMapper = reviewMapper;
    }


    @PostMapping
    public ResponseEntity<ReviewResponse> create(
            @Valid @RequestBody ReviewRequest request
    ) {

        User user = userService.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                );
        Movie movie = movieService.getMovieById(request.getMovieId());


        Review created = reviewService.createReview(
                user.getId(), movie.getId(),
                request.getRating(), request.getComment()
        );

        return new ResponseEntity<>(
                reviewMapper.toDto(created),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ReviewResponse update(
            @PathVariable Long id,
            @Valid @RequestBody ReviewUpdateRequest request   // ← change here
    ) {
        Review updated = reviewService.updateReview(
                id,
                request.getRating(),    // ← extract only rating
                request.getComment()    // ← extract only comment
        );
        return reviewMapper.toDto(updated);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }


    @GetMapping
    public Page<ReviewResponse> list(
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) Long userId,
            Pageable pageable
    ) {
        Page<Review> page;
        if (movieId != null) {
            page = reviewService.getReviewsByMovie(movieId, pageable);
        } else if (userId != null) {
            page = reviewService.getReviewsByUser(userId, pageable);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Must provide movieId or userId"
            );
        }
        return page.map(reviewMapper::toDto);
    }
}
