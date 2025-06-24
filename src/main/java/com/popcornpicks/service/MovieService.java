package com.popcornpicks.service;

import com.popcornpicks.models.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface MovieService {

    Page<Movie> getAllMovies(Pageable pageable);


    Page<Movie> searchByTitle(String title, Pageable pageable);


    Page<Movie> filterByGenre(String genre, Pageable pageable);


    Page<Movie> filterByYear(int year, Pageable pageable);


    Movie getMovieById(Long id);


    Movie createMovie(Movie movie);


    Movie updateMovie(Long id, Movie movie);


    void deleteMovie(Long id);


    Page<Movie> getMoviesAboveRating(double rating, Pageable pageable);


    Page<Movie> getMoviesCreatedAfter(LocalDateTime dateTime, Pageable pageable);


    Page<Movie> getMoviesOrderedByDate(Pageable pageable);
}
