package com.popcornpicks.repository;

import com.popcornpicks.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


    Optional<Review> findByUserIdAndMovieId(Long userId, Long movieId);


    Page<Review> findByMovieId(Long movieId, Pageable pageable);


    Page<Review> findByUserId(Long userId, Pageable pageable);


    List<Review> findByMovieId(Long movieId);
}
