package com.popcornpicks.service.impl;

import com.popcornpicks.models.Movie;
import com.popcornpicks.repository.MovieRepository;
import com.popcornpicks.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

        Movie movie = new Movie();
        movie.setTitle("Inception");
        List<Movie> movieList = List.of(movie);
        Pageable pageable = Pageable.unpaged();
        when(movieRepository.findAll(pageable)).thenReturn(new PageImpl<>(movieList));


        var result = movieService.getAllMovies(pageable);


        assertEquals(1, result.getTotalElements());
        assertEquals("Inception", result.getContent().get(0).getTitle());
        verify(movieRepository, times(1)).findAll(pageable);
    }

    @Test
    void testSearchByTitle_ReturnsMatchingMovies() {

        String searchTerm = "matrix";
        Movie movie = new Movie();
        movie.setTitle("The Matrix");
        List<Movie> movies = List.of(movie);
        Pageable pageable = Pageable.unpaged();
        when(movieRepository.findByTitleContainingIgnoreCase(searchTerm, pageable))
                .thenReturn(new PageImpl<>(movies));


        var result = movieService.searchByTitle(searchTerm, pageable);


        assertEquals(1, result.getTotalElements());
        assertEquals("The Matrix", result.getContent().get(0).getTitle());
        verify(movieRepository, times(1)).findByTitleContainingIgnoreCase(searchTerm, pageable);
    }

    @Test
    void testFilterByGenre_ReturnsFilteredMovies() {

        String genre = "Action";
        Movie movie = new Movie();
        movie.setTitle("Mad Max");
        movie.setGenres(List.of("Action"));
        List<Movie> movies = List.of(movie);
        Pageable pageable = Pageable.unpaged();
        when(movieRepository.findByGenresContaining(genre, pageable))
                .thenReturn(new PageImpl<>(movies));


        var result = movieService.filterByGenre(genre, pageable);


        assertEquals(1, result.getTotalElements());
        assertEquals("Mad Max", result.getContent().get(0).getTitle());
        verify(movieRepository, times(1)).findByGenresContaining(genre, pageable);
    }

    @Test
    void testFilterByYear_ReturnsMoviesFromThatYear() {

        int year = 1999;
        Movie movie = new Movie();
        movie.setTitle("Fight Club");
        movie.setYear(year);
        List<Movie> movies = List.of(movie);
        Pageable pageable = Pageable.unpaged();
        when(movieRepository.findByYear(year, pageable))
                .thenReturn(new PageImpl<>(movies));


        var result = movieService.filterByYear(year, pageable);


        assertEquals(1, result.getTotalElements());
        assertEquals(1999, result.getContent().get(0).getYear());
        verify(movieRepository, times(1)).findByYear(year, pageable);
    }

    @Test
    void testGetMovieById_WhenMovieExists_ReturnsMovie() {

        Long movieId = 1L;
        Movie movie = new Movie();
        movie.setTitle("Interstellar");

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));


        Movie result = movieService.getMovieById(movieId);


        assertNotNull(result);
        assertEquals("Interstellar", result.getTitle());
        verify(movieRepository, times(1)).findById(movieId);
    }

    @Test
    void testGetMovieById_WhenMovieNotFound_ThrowsException() {

        Long movieId = 999L;
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> movieService.getMovieById(movieId)
        );

        assertEquals("Movie not found with id 999", exception.getMessage());
        verify(movieRepository, times(1)).findById(movieId);
    }

    @Test
    void testCreateMovie_SetsInitialRatingAndSaves() {

        Movie inputMovie = new Movie();
        inputMovie.setTitle("Oppenheimer");

        Movie savedMovie = new Movie();
        savedMovie.setTitle("Oppenheimer");
        savedMovie.setAverageRating(0.0);

        when(movieRepository.save(inputMovie)).thenReturn(savedMovie);


        Movie result = movieService.createMovie(inputMovie);


        assertEquals("Oppenheimer", result.getTitle());
        assertEquals(0.0, result.getAverageRating());
        verify(movieRepository, times(1)).save(inputMovie);
    }

    @Test
    void testUpdateMovie_UpdatesFieldsAndSaves() {

        Long movieId = 1L;

        Movie existingMovie = new Movie();
        existingMovie.setTitle("Old Title");
        existingMovie.setYear(2000);
        existingMovie.setPosterPath("old.jpg");
        existingMovie.setGenres(List.of("Drama"));
        existingMovie.setAverageRating(4.2);

        Movie updatedMovie = new Movie();
        updatedMovie.setTitle("New Title");
        updatedMovie.setYear(2024);
        updatedMovie.setPosterPath("new.jpg");
        updatedMovie.setGenres(List.of("Sci-Fi"));

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(existingMovie)).thenReturn(existingMovie);


        Movie result = movieService.updateMovie(movieId, updatedMovie);


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

        Long movieId = 1L;
        Movie existingMovie = new Movie();
        existingMovie.setTitle("To Delete");

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));


        movieService.deleteMovie(movieId);


        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, times(1)).delete(existingMovie);
    }

    @Test
    void testGetMoviesAboveRating_ReturnsMatchingMovies() {

        double ratingThreshold = 4.0;
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setAverageRating(4.8);

        List<Movie> movies = List.of(movie);
        Pageable pageable = Pageable.unpaged();

        when(movieRepository.findByAverageRatingGreaterThan(ratingThreshold, pageable))
                .thenReturn(new PageImpl<>(movies));


        var result = movieService.getMoviesAboveRating(ratingThreshold, pageable);


        assertEquals(1, result.getTotalElements());
        assertEquals("Inception", result.getContent().get(0).getTitle());
        assertTrue(result.getContent().get(0).getAverageRating() > 4.0);
        verify(movieRepository, times(1)).findByAverageRatingGreaterThan(ratingThreshold, pageable);
    }

    @Test
    void testGetMoviesCreatedAfter_ReturnsRecentMovies() {

        LocalDateTime since = LocalDateTime.now().minusDays(7);
        Movie movie = new Movie();
        movie.setTitle("New Release");

        List<Movie> movies = List.of(movie);
        Pageable pageable = Pageable.unpaged();

        when(movieRepository.findByCreatedAtAfter(since, pageable))
                .thenReturn(new PageImpl<>(movies));


        var result = movieService.getMoviesCreatedAfter(since, pageable);


        assertEquals(1, result.getTotalElements());
        assertEquals("New Release", result.getContent().get(0).getTitle());
        verify(movieRepository, times(1)).findByCreatedAtAfter(since, pageable);
    }

    @Test
    void testGetMoviesOrderedByDate_ReturnsSortedMovies() {

        Movie movie = new Movie();
        movie.setTitle("Latest Movie");
        Pageable pageable = Pageable.unpaged();

        when(movieRepository.findAllByOrderByCreatedAtDesc(pageable))
                .thenReturn(new PageImpl<>(List.of(movie)));


        var result = movieService.getMoviesOrderedByDate(pageable);


        assertEquals(1, result.getTotalElements());
        assertEquals("Latest Movie", result.getContent().get(0).getTitle());
        verify(movieRepository, times(1)).findAllByOrderByCreatedAtDesc(pageable);
    }


}
