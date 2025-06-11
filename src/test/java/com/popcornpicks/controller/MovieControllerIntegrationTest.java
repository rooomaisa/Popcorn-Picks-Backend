package com.popcornpicks.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;



import com.popcornpicks.models.Movie;
import com.popcornpicks.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import java.time.LocalDateTime;
import java.util.List;



@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    private Long testMovieId;

    @BeforeEach
    void setUp() {
        // 1) wipe any leftover data
        movieRepository.deleteAll();

        // 2) insert exactly one Movie
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setYear(2024);
        movie.setPosterPath("test.jpg");
        movie.setAverageRating(0.0);
        movie.setGenres(List.of("Drama"));  // adjust genres as needed

        // 3) save & stash its ID for use in your tests
        testMovieId = movieRepository.save(movie).getId();
    }



    @Test
    @WithMockUser
    void testGetAllMovies_Returns200() throws Exception {
        mockMvc.perform(get("/api/v1/movies"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testSearchByTitle_Returns200() throws Exception {
        mockMvc.perform(get("/api/v1/movies")
                        .param("title", "matrix"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testFilterByGenre_Returns200() throws Exception {
        mockMvc.perform(get("/api/v1/movies")
                        .param("genre", "action"))
                .andExpect(status().isOk());
    }


    @Test @WithMockUser
    void getMovieById_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/movies/" + testMovieId))
                .andExpect(status().isOk());
    }

    @Test
    void testUploadFile_UnauthenticatedUserGets403() throws Exception {
        mockMvc.perform(post("/api/v1/files/upload"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllMovies_ReturnsOkStatus() throws Exception {
        mockMvc.perform(get("/api/v1/movies"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getMovieById_ExistingId_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/movies/" + testMovieId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testFilterByYear_Returns200() throws Exception {
        mockMvc.perform(get("/api/v1/movies")
                        .param("year", "2024"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetMoviesAboveRating_Returns200() throws Exception {
        mockMvc.perform(get("/api/v1/movies")
                        .param("ratingMin", "1.0"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetMoviesOrderedByDate_Returns200() throws Exception {
        mockMvc.perform(get("/api/v1/movies/order-by-date"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getMoviesAboveRating_Returns200() throws Exception {
        mockMvc.perform(get("/api/v1/movies/above-rating")
                        .param("rating", "3.0"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getMoviesCreatedAfter_Returns200() throws Exception {
        String timestamp = LocalDateTime.now().minusDays(1).toString();

        mockMvc.perform(get("/api/v1/movies/created-after")
                        .param("timestamp", timestamp))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreateMovie_AsUser_Returns403() throws Exception {
        String newMovieJson = "...";

        mockMvc.perform(post("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newMovieJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateMovie_AsAdmin_Returns201() throws Exception {
        String newMovieJson = """
        {
          "title":      "The Matrix",
          "year":       1999,
          "posterPath": "/uploads/matrix.jpg",
          "genres":     ["Sci-Fi","Action"]
        }
    """;

        mockMvc.perform(post("/api/v1/movies")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newMovieJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("The Matrix"))
                .andExpect(jsonPath("$.year").value(1999));
    }

    // ————————————————————————————
    // UPDATE (PUT) ➞ only ADMIN
    // ————————————————————————————
    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateMovie_AsAdmin_Returns200() throws Exception {
        String updateJson = """
        {
          "title":      "Updated Movie",
          "year":       2025,
          "posterPath": "/uploads/updated.jpg",
          "genres":     ["Comedy"]
        }
        """;

        mockMvc.perform(put("/api/v1/movies/" + testMovieId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Movie"))
                .andExpect(jsonPath("$.year").value(2025));
    }

    @Test
    @WithMockUser    // no roles ⇒ forbidden
    void testUpdateMovie_AsUser_Returns403() throws Exception {
        String updateJson = "{\"title\":\"X\"}";

        mockMvc.perform(put("/api/v1/movies/" + testMovieId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isForbidden());
    }

    // ————————————————————————————
    // DELETE ➞ only ADMIN
    // ————————————————————————————
    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteMovie_AsAdmin_Returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/movies/" + testMovieId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser    // no roles ⇒ forbidden
    void testDeleteMovie_AsUser_Returns403() throws Exception {
        mockMvc.perform(delete("/api/v1/movies/" + testMovieId)
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    // ————————————————————————————
    // VALIDATION ERRORS
    // ————————————————————————————
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateMovie_BadPayload_Returns400() throws Exception {
        // missing required 'title'
        String badJson = "{\"year\":1800}";

        mockMvc.perform(post("/api/v1/movies")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest());
    }


}






















