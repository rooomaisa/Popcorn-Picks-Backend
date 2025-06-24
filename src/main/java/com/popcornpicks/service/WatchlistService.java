package com.popcornpicks.service;

import com.popcornpicks.models.WatchlistItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WatchlistService {


    WatchlistItem addToWatchlist(Long userId, Long movieId);


    void removeFromWatchlist(Long userId, Long movieId);


    Page<WatchlistItem> getWatchlist(Long userId, Pageable pageable);
}
