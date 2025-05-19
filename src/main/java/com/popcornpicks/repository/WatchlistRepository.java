package com.popcornpicks.repository;

import com.popcornpicks.models.WatchlistItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WatchlistRepository extends JpaRepository<WatchlistItem, Long> {

    /** Prevent duplicates: find an existing entry for user+movie */
    Optional<WatchlistItem> findByUserIdAndMovieId(Long userId, Long movieId);

    /** List all watchlist items for a given user */
    Page<WatchlistItem> findByUserId(Long userId, Pageable pageable);

    /** Convenience: delete by user and movie */
    void deleteByUserIdAndMovieId(Long userId, Long movieId);
}
