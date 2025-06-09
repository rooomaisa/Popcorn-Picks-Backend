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

import java.time.LocalDateTime;
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

    @Test
    void testFilterByYear_ReturnsMoviesFromThatYear() {
        // Arrange
        int year = 1999;
        Movie movie = new Movie();
        movie.setTitle("Fight Club");
        movie.setYear(year);
        List<Movie> movies = List.of(movie);
        Pageable pageable = Pageable.unpaged();
        when(movieRepository.findByYear(year, pageable))
                .thenReturn(new PageImpl<>(movies));

        // Act
        var result = movieService.filterByYear(year, pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(1999, result.getContent().get(0).getYear());
        verify(movieRepository, times(1)).findByYear(year, pageable);
    }

    @Test
    void testGetMovieById_WhenMovieExists_ReturnsMovie() {
        // Arrange
        Long movieId = 1L;
        Movie movie = new Movie();
        movie.setTitle("Interstellar");

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        // Act
        Movie result = movieService.getMovieById(movieId);

        // Assert
        assertNotNull(result);
        assertEquals("Interstellar", result.getTitle());
        verify(movieRepository, times(1)).findById(movieId);
    }

    @Test
    void testGetMovieById_WhenMovieNotFound_ThrowsException() {
        // Arrange
        Long movieId = 999L;
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> movieService.getMovieById(movieId)
        );

        assertEquals("Movie not found with id 999", exception.getMessage());
        verify(movieRepository, times(1)).findById(movieId);
    }

    @Test
    void testCreateMovie_SetsInitialRatingAndSaves() {
        // Arrange
        Movie inputMovie = new Movie();
        inputMovie.setTitle("Oppenheimer");

        Movie savedMovie = new Movie();
        savedMovie.setTitle("Oppenheimer");
        savedMovie.setAverageRating(0.0);

        when(movieRepository.save(inputMovie)).thenReturn(savedMovie);

        // Act
        Movie result = movieService.createMovie(inputMovie);

        // Assert
        assertEquals("Oppenheimer", result.getTitle());
        assertEquals(0.0, result.getAverageRating());
        verify(movieRepository, times(1)).save(inputMovie);
    }

    @Test
    void testUpdateMovie_UpdatesFieldsAndSaves() {
        // Arrange
        Long movieId = 1L;

        Movie existingMovie = new Movie();
        existingMovie.setTitle("Old Title");
        existingMovie.setYear(2000);
        existingMovie.setPosterPath("old.jpg");
        existingMovie.setGenres(List.of("Drama"));
        existingMovie.setAverageRating(4.2); // stays unchanged

        Movie updatedMovie = new Movie();
        updatedMovie.setTitle("New Title");
        updatedMovie.setYear(2024);
        updatedMovie.setPosterPath("new.jpg");
        updatedMovie.setGenres(List.of("Sci-Fi"));

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(existingMovie)).thenReturn(existingMovie);

        // Act
        Movie result = movieService.updateMovie(movieId, updatedMovie);

        // Assert
        assertEquals("New Title", result.getTitle());
        assertEquals(2024, result.getYear());
        assertEquals("new.jpg", result.getPosterPath());
        assertEquals(List.of("Sci-Fi"), result.getGenres());
        assertEquals(4.2, result.getAverageRating()); // stays the same

        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, times(1)).save(existingMovie);
    }

    @Test
    void testDeleteMovie_RemovesMovie() {
        // Arrange
        Long movieId = 1L;
        Movie existingMovie = new Movie();
        existingMovie.setTitle("To Delete");

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));

        // Act
        movieService.deleteMovie(movieId);

        // Assert
        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, times(1)).delete(existingMovie);
    }

    @Test
    void testGetMoviesAboveRating_ReturnsMatchingMovies() {
        // Arrange
        double ratingThreshold = 4.0;
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setAverageRating(4.8);

        List<Movie> movies = List.of(movie);
        Pageable pageable = Pageable.unpaged();

        when(movieRepository.findByAverageRatingGreaterThan(ratingThreshold, pageable))
                .thenReturn(new PageImpl<>(movies));

        // Act
        var result = movieService.getMoviesAboveRating(ratingThreshold, pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("Inception", result.getContent().get(0).getTitle());
        assertTrue(result.getContent().get(0).getAverageRating() > 4.0);
        verify(movieRepository, times(1)).findByAverageRatingGreaterThan(ratingThreshold, pageable);
    }

    @Test
    void testGetMoviesCreatedAfter_ReturnsRecentMovies() {
        // Arrange
        LocalDateTime since = LocalDateTime.now().minusDays(7);
        Movie movie = new Movie();
        movie.setTitle("New Release");

        List<Movie> movies = List.of(movie);
        Pageable pageable = Pageable.unpaged();

        when(movieRepository.findByCreatedAtAfter(since, pageable))
                .thenReturn(new PageImpl<>(movies));

        // Act
        var result = movieService.getMoviesCreatedAfter(since, pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("New Release", result.getContent().get(0).getTitle());
        verify(movieRepository, times(1)).findByCreatedAtAfter(since, pageable);
    }

    @Test
    void testGetMoviesOrderedByDate_ReturnsSortedMovies() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("Latest Movie");
        Pageable pageable = Pageable.unpaged();

        when(movieRepository.findAllByOrderByCreatedAtDesc(pageable))
                .thenReturn(new PageImpl<>(List.of(movie)));

        // Act
        var result = movieService.getMoviesOrderedByDate(pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("Latest Movie", result.getContent().get(0).getTitle());
        verify(movieRepository, times(1)).findAllByOrderByCreatedAtDesc(pageable);
    }


}
