package com.popcornpicks.repository;

import com.popcornpicks.models.WatchlistItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WatchlistRepository extends JpaRepository<WatchlistItem, Long> {


    Optional<WatchlistItem> findByUserIdAndMovieId(Long userId, Long movieId);


    Page<WatchlistItem> findByUserId(Long userId, Pageable pageable);


    void deleteByUserIdAndMovieId(Long userId, Long movieId);
}
