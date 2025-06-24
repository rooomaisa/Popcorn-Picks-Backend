package com.popcornpicks.service.impl;

import com.popcornpicks.exceptions.ResourceNotFoundException;
import com.popcornpicks.models.Movie;
import com.popcornpicks.models.Review;
import com.popcornpicks.models.User;
import com.popcornpicks.repository.MovieRepository;
import com.popcornpicks.repository.ReviewRepository;
import com.popcornpicks.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.popcornpicks.exceptions.DuplicateReviewException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;



class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private final Long REVIEW_ID = 1L;
    private final Long MOVIE_ID  = 2L;
    private final Long USER_ID   = 3L;

    private Movie movie;
    private User user;
    private Review existingReview;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        movie = new Movie();
        movie.setId(MOVIE_ID);

        user = new User();
        user.setId(USER_ID);

        existingReview = new Review();
        existingReview.setId(REVIEW_ID);
        existingReview.setMovie(movie);
        existingReview.setUser(user);
        existingReview.setRating(4);
        existingReview.setComment("Great!");
    }

    @Test
    void testCreateReview_Success() {
        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(movie));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(reviewRepository.save(any(Review.class))).thenAnswer(inv -> {
            Review r = inv.getArgument(0);
            r.setId(REVIEW_ID);
            return r;
        });

        Review saved = reviewService.createReview( USER_ID, MOVIE_ID, 5, "Awesome");

        assertNotNull(saved.getId());
        assertEquals(5, saved.getRating());
        assertEquals("Awesome", saved.getComment());
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void testCreateReview_MovieNotFound() {

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.empty());


        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> reviewService.createReview(USER_ID, MOVIE_ID, 3, "Nice")
        );
        assertTrue(ex.getMessage().contains("Movie not found with id " + MOVIE_ID));
    }


    @Test
    void testGetReviewById_Success() {
        when(reviewRepository.findById(REVIEW_ID)).thenReturn(Optional.of(existingReview));

        Review found = reviewService.getReviewById(REVIEW_ID);

        assertEquals(REVIEW_ID, found.getId());
        assertEquals("Great!", found.getComment());
    }

    @Test
    void testGetReviewById_NotFound() {

        when(reviewRepository.findById(REVIEW_ID)).thenReturn(Optional.empty());


        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> reviewService.getReviewById(REVIEW_ID)
        );
        assertTrue(ex.getMessage().contains("Review not found with id " + REVIEW_ID));
    }

    @Test
    void testUpdateReview_NotFound() {
        when(reviewRepository.findById(REVIEW_ID))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> reviewService.updateReview(REVIEW_ID, 1, "Bad")
        );
        assertTrue(ex.getMessage()
                .contains("Review not found with id " + REVIEW_ID));
    }

    @Test
    void testDeleteReview_Success() {

        when(reviewRepository.findById(REVIEW_ID))
                .thenReturn(Optional.of(existingReview));

        when(reviewRepository.findByMovieId(MOVIE_ID))
                .thenReturn(List.of(existingReview));
        when(movieRepository.findById(MOVIE_ID))
                .thenReturn(Optional.of(movie));
        when(movieRepository.save(movie))
                .thenReturn(movie);


        assertDoesNotThrow(() -> reviewService.deleteReview(REVIEW_ID));

        verify(reviewRepository).delete(existingReview);
        verify(movieRepository).save(movie);
    }

    @Test
    void testDeleteReview_NotFound() {
        when(reviewRepository.findById(REVIEW_ID))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> reviewService.deleteReview(REVIEW_ID)
        );
        assertTrue(ex.getMessage()
                .contains("Review not found with id " + REVIEW_ID));
    }

    @Test
    void testCreateReview_RatingTooLow() {
        assertThrows(IllegalArgumentException.class,
                () -> reviewService.createReview(USER_ID, MOVIE_ID, 0, "Nope")
        );
    }

    @Test
    void testCreateReview_RatingTooHigh() {
        assertThrows(IllegalArgumentException.class,
                () -> reviewService.createReview(USER_ID, MOVIE_ID, 6, "Nope")
        );
    }

    @Test
    void testCreateReview_UserNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> reviewService.createReview(USER_ID, MOVIE_ID, 3, "Nice")
        );
        assertTrue(ex.getMessage().contains("User not found with id " + USER_ID));
    }

    @Test
    void testCreateReview_DuplicateReview() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(movie));
        when(reviewRepository.findByUserIdAndMovieId(USER_ID, MOVIE_ID))
                .thenReturn(Optional.of(existingReview));

        DuplicateReviewException ex = assertThrows(
                DuplicateReviewException.class,
                () -> reviewService.createReview(USER_ID, MOVIE_ID, 4, "Dup")
        );
        assertTrue(ex.getMessage().contains("Review already exists for user " + USER_ID));
    }



    @Test
    void testUpdateReview_Success() {
        when(reviewRepository.findById(REVIEW_ID))
                .thenReturn(Optional.of(existingReview));
        when(reviewRepository.save(existingReview))
                .thenReturn(existingReview);

        when(reviewRepository.findByMovieId(MOVIE_ID))
                .thenReturn(List.of(existingReview));
        when(movieRepository.findById(MOVIE_ID))
                .thenReturn(Optional.of(movie));
        when(movieRepository.save(movie))
                .thenReturn(movie);

        Review updated = reviewService.updateReview(REVIEW_ID, 2, "So-so");

        assertEquals(2, updated.getRating());
        assertEquals("So-so", updated.getComment());
        verify(reviewRepository).save(existingReview);
        verify(movieRepository).save(movie);
    }

    @Test
    void testUpdateReview_RatingTooLow() {
        assertThrows(IllegalArgumentException.class,
                () -> reviewService.updateReview(REVIEW_ID, 0, "Bad")
        );
    }

    @Test
    void testUpdateReview_RatingTooHigh() {
        assertThrows(IllegalArgumentException.class,
                () -> reviewService.updateReview(REVIEW_ID, 6, "Bad")
        );
    }



    @Test
    void testGetReviewsByMovie_NotFound() {
        when(movieRepository.existsById(MOVIE_ID)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> reviewService.getReviewsByMovie(MOVIE_ID, Pageable.unpaged())
        );
        assertTrue(ex.getMessage().contains("Movie not found with id " + MOVIE_ID));
    }

    @Test
    void testGetReviewsByMovie_Success() {
        when(movieRepository.existsById(MOVIE_ID)).thenReturn(true);
        Page<Review> page = new PageImpl<>(List.of(existingReview));
        when(reviewRepository.findByMovieId(MOVIE_ID, Pageable.unpaged()))
                .thenReturn(page);

        Page<Review> result = reviewService.getReviewsByMovie(MOVIE_ID, Pageable.unpaged());
        assertEquals(1, result.getTotalElements());
        verify(reviewRepository).findByMovieId(MOVIE_ID, Pageable.unpaged());
    }



    @Test
    void testGetReviewsByUser_NotFound() {
        when(userRepository.existsById(USER_ID)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> reviewService.getReviewsByUser(USER_ID, Pageable.unpaged())
        );
        assertTrue(ex.getMessage().contains("User not found with id " + USER_ID));
    }

    @Test
    void testGetReviewsByUser_Success() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        Page<Review> page = new PageImpl<>(List.of(existingReview));
        when(reviewRepository.findByUserId(USER_ID, Pageable.unpaged()))
                .thenReturn(page);

        Page<Review> result = reviewService.getReviewsByUser(USER_ID, Pageable.unpaged());
        assertEquals(1, result.getTotalElements());
        verify(reviewRepository).findByUserId(USER_ID, Pageable.unpaged());
    }


}

