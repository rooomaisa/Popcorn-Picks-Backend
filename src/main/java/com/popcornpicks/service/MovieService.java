package com.popcornpicks.service;

import com.popcornpicks.models.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface MovieService {
    /** List all movies, paginated */
    Page<Movie> getAllMovies(Pageable pageable);

    /** Search by title (contains, case-insensitive) */
    Page<Movie> searchByTitle(String title, Pageable pageable);

    /** Filter by genre */
    Page<Movie> filterByGenre(String genre, Pageable pageable);

    /** Filter by release year */
    Page<Movie> filterByYear(int year, Pageable pageable);

    /** Fetch a single movie or throw if not found */
    Movie getMovieById(Long id);

    /** Create a new movie (averageRating initialized to 0.0) */
    Movie createMovie(Movie movie);

    /** Update an existing movie’s fields */
    Movie updateMovie(Long id, Movie movie);

    /** Delete a movie by ID */
    void deleteMovie(Long id);

    /** Admin: movies with rating > threshold */
    Page<Movie> getMoviesAboveRating(double rating, Pageable pageable);

    /** Admin: movies created after a given timestamp */
    Page<Movie> getMoviesCreatedAfter(LocalDateTime dateTime, Pageable pageable);

    /** Admin: list movies ordered by newest first */
    Page<Movie> getMoviesOrderedByDate(Pageable pageable);
}
