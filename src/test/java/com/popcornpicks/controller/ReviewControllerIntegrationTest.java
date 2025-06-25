package com.popcornpicks.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.popcornpicks.models.Review;
import com.popcornpicks.models.Movie;
import com.popcornpicks.models.User;
import com.popcornpicks.repository.ReviewRepository;
import com.popcornpicks.repository.MovieRepository;
import com.popcornpicks.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    private Long testMovieId;
    private Long testReviewId;
    private Long testUserId;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
        movieRepository.deleteAll();
        userRepository.deleteAll();

        // create test user
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        testUserId = userRepository.save(user).getId();

        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setYear(2024);
        movie.setPosterPath("test.jpg");
        movie.setAverageRating(0.0);
        movie.setGenres(List.of("Drama"));
        testMovieId = movieRepository.save(movie).getId();

        Review review = new Review();
        review.setMovie(movie);
        review.setUser(user);
        review.setRating(5);
        review.setComment("Excellent!");
        testReviewId = reviewRepository.save(review).getId();
    }

    @Test
    void testCreateReview_UnauthenticatedUserGets403() throws Exception {
        String newReviewJson = String.format("""
        {
          "userId": %d,
          "movieId": %d,
          "rating": 3,
          "comment": "Nice"
        }
        """, testUserId, testMovieId);

        mockMvc.perform(post("/api/v1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newReviewJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testCreateReview_AsAuthenticated_Returns201() throws Exception {
        reviewRepository.deleteAll();

        String newReviewJson = String.format("""
        {
          "userId": %d,
          "movieId": %d,
          "rating": 4,
          "comment": "Great movie!"
        }
        """, testUserId, testMovieId);

        mockMvc.perform(post("/api/v1/reviews")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newReviewJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.rating").value(4))
                .andExpect(jsonPath("$.comment").value("Great movie!"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testGetReviewsByMovie_Returns200() throws Exception {
        mockMvc.perform(get("/api/v1/reviews")
                        .param("movieId", testMovieId.toString()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testListWithoutParams_Returns400() throws Exception {
        mockMvc.perform(get("/api/v1/reviews"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testUpdateReview_AsAuthenticated_Returns200() throws Exception {
        String updateJson = """
        {
          "rating": 2,
          "comment": "Changed comment"
        }
        """;

        mockMvc.perform(put("/api/v1/reviews/" + testReviewId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(2))
                .andExpect(jsonPath("$.comment").value("Changed comment"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testDeleteReview_AsAuthenticated_Returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/reviews/" + testReviewId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
