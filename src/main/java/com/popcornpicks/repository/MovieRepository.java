package com.popcornpicks.repository;

import com.popcornpicks.models.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {


    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Movie> findByGenresContaining(String genre, Pageable pageable);
    Page<Movie> findByYear(int year, Pageable pageable);



    Page<Movie> findByAverageRatingGreaterThan(double rating, Pageable pageable);
    Page<Movie> findByCreatedAtAfter(LocalDateTime dateTime, Pageable pageable);
    Page<Movie> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
