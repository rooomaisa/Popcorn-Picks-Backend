package com.popcornpicks.service.impl;

import com.popcornpicks.exceptions.ResourceNotFoundException;
import com.popcornpicks.models.Movie;
import com.popcornpicks.repository.MovieRepository;
import com.popcornpicks.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    @Override
    public Page<Movie> searchByTitle(String title, Pageable pageable) {
        return movieRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    @Override
    public Page<Movie> filterByGenre(String genre, Pageable pageable) {
        return movieRepository.findByGenresContaining(genre, pageable);
    }

    @Override
    public Page<Movie> filterByYear(int year, Pageable pageable) {
        return movieRepository.findByYear(year, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + id));
    }

    @Override
    public Movie createMovie(Movie movie) {

        movie.setAverageRating(0.0);
        return movieRepository.save(movie);
    }

    @Override
    public Movie updateMovie(Long id, Movie updated) {
        Movie existing = getMovieById(id);
        existing.setTitle(updated.getTitle());
        existing.setYear(updated.getYear());
        existing.setPosterPath(updated.getPosterPath());
        existing.setGenres(updated.getGenres());

        return movieRepository.save(existing);
    }

    @Override
    public void deleteMovie(Long id) {
        Movie existing = getMovieById(id);
        movieRepository.delete(existing);
    }

    @Override
    public Page<Movie> getMoviesAboveRating(double rating, Pageable pageable) {
        return movieRepository.findByAverageRatingGreaterThan(rating, pageable);
    }

    @Override
    public Page<Movie> getMoviesCreatedAfter(LocalDateTime dateTime, Pageable pageable) {
        return movieRepository.findByCreatedAtAfter(dateTime, pageable);
    }

    @Override
    public Page<Movie> getMoviesOrderedByDate(Pageable pageable) {
        return movieRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
}
