package com.popcornpicks.controllers;

import com.popcornpicks.dto.MovieRequest;
import com.popcornpicks.dto.MovieResponse;
import com.popcornpicks.mapper.MovieMapper;
import com.popcornpicks.models.Movie;
import com.popcornpicks.service.MovieService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final MovieService movieService;
    private final MovieMapper movieMapper;

    @Autowired
    public MovieController(MovieService movieService, MovieMapper movieMapper) {
        this.movieService = movieService;
        this.movieMapper = movieMapper;
    }

    /** List or filter movies:
     *  ?title=…   or ?genre=…   or ?year=…
     */
    @GetMapping
    public Page<MovieResponse> list(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer year,
            Pageable pageable
    ) {
        Page<Movie> page;
        if (title != null) {
            page = movieService.searchByTitle(title, pageable);
        } else if (genre != null) {
            page = movieService.filterByGenre(genre, pageable);
        } else if (year != null) {
            page = movieService.filterByYear(year, pageable);
        } else {
            page = movieService.getAllMovies(pageable);
        }
        return page.map(movieMapper::toDto);
    }

    /** Fetch a single movie by ID */
    @GetMapping("/{id}")
    public MovieResponse getById(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id);
        return movieMapper.toDto(movie);
    }

    /** Create a new movie (Admin later) */
    @PostMapping
    public ResponseEntity<MovieResponse> create(
            @Valid @RequestBody MovieRequest request
    ) {
        Movie toCreate = movieMapper.toEntity(request);
        Movie created = movieService.createMovie(toCreate);
        return new ResponseEntity<>(
                movieMapper.toDto(created),
                HttpStatus.CREATED
        );
    }

    /** Update an existing movie (Admin later) */
    @PutMapping("/{id}")
    public MovieResponse update(
            @PathVariable Long id,
            @Valid @RequestBody MovieRequest request
    ) {
        Movie toUpdate = movieMapper.toEntity(request);
        Movie updated = movieService.updateMovie(id, toUpdate);
        return movieMapper.toDto(updated);
    }

    /** Delete a movie by ID (Admin later) */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }
}
