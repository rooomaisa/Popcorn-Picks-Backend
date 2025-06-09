package com.popcornpicks.service.impl;

import com.popcornpicks.models.Movie;
import com.popcornpicks.repository.MovieRepository;
import com.popcornpicks.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceImplTest {

    private MovieRepository movieRepository;
    private MovieServiceImpl movieService;

    @BeforeEach
    void setUp() {
        movieRepository = mock(MovieRepository.class);
        movieService = new MovieServiceImpl(movieRepository);
    }

    @Test
    void testGetAllMovies_ReturnsPageOfMovies() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("Inception");
        List<Movie> movieList = List.of(movie);
        Pageable pageable = Pageable.unpaged();
        when(movieRepository.findAll(pageable)).thenReturn(new PageImpl<>(movieList));

        // Act
        var result = movieService.getAllMovies(pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("Inception", result.getContent().get(0).getTitle());
        verify(movieRepository, times(1)).findAll(pageable);
    }

}
