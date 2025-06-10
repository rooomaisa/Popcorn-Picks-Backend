package com.popcornpicks.controller;

import com.popcornpicks.models.Movie;
import com.popcornpicks.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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









}
