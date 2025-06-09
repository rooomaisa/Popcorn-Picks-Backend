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

    @Test
    void testSearchByTitle_ReturnsMatchingMovies() {
        // Arrange
        String searchTerm = "matrix";
        Movie movie = new Movie();
        movie.setTitle("The Matrix");
        List<Movie> movies = List.of(movie);
        Pageable pageable = Pageable.unpaged();
        when(movieRepository.findByTitleContainingIgnoreCase(searchTerm, pageable))
                .thenReturn(new PageImpl<>(movies));

        // Act
        var result = movieService.searchByTitle(searchTerm, pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("The Matrix", result.getContent().get(0).getTitle());
        verify(movieRepository, times(1)).findByTitleContainingIgnoreCase(searchTerm, pageable);
    }

    @Test
    void testFilterByGenre_ReturnsFilteredMovies() {
        // Arrange
        String genre = "Action";
        Movie movie = new Movie();
        movie.setTitle("Mad Max");
        movie.setGenres(List.of("Action"));
        List<Movie> movies = List.of(movie);
        Pageable pageable = Pageable.unpaged();
        when(movieRepository.findByGenresContaining(genre, pageable))
                .thenReturn(new PageImpl<>(movies));

        // Act
        var result = movieService.filterByGenre(genre, pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("Mad Max", result.getContent().get(0).getTitle());
        verify(movieRepository, times(1)).findByGenresContaining(genre, pageable);
    }



}
