package com.popcornpicks.mapper;

import com.popcornpicks.dto.MovieRequest;
import com.popcornpicks.dto.MovieResponse;
import com.popcornpicks.models.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {


    public Movie toEntity(MovieRequest req) {
        return new Movie(
                req.getTitle(),
                req.getYear(),
                req.getPosterPath(),
                req.getGenres()
        );
    }


    public MovieResponse toDto(Movie movie) {
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getYear(),
                movie.getPosterPath(),
                movie.getAverageRating(),
                movie.getGenres(),
                movie.getCreatedAt()
        );
    }
}
