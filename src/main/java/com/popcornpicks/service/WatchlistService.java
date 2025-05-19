package com.popcornpicks.service;

import com.popcornpicks.models.WatchlistItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WatchlistService {

    /** Add a movie to the user’s watchlist; no-ops on duplicate */
    WatchlistItem addToWatchlist(Long userId, Long movieId);

    /** Remove a movie from the user’s watchlist */
    void removeFromWatchlist(Long userId, Long movieId);

    /** Fetch the user’s watchlist, paginated */
    Page<WatchlistItem> getWatchlist(Long userId, Pageable pageable);
}
