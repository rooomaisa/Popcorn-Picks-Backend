package com.popcornpicks.service.impl;

import com.popcornpicks.models.Movie;
import com.popcornpicks.models.User;
import com.popcornpicks.models.WatchlistItem;
import com.popcornpicks.repository.MovieRepository;
import com.popcornpicks.repository.UserRepository;
import com.popcornpicks.repository.WatchlistRepository;
import com.popcornpicks.service.WatchlistService;
import com.popcornpicks.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WatchlistServiceImpl implements WatchlistService {

    private final WatchlistRepository watchlistRepo;
    private final UserRepository userRepo;
    private final MovieRepository movieRepo;

    @Autowired
    public WatchlistServiceImpl(
            WatchlistRepository watchlistRepo,
            UserRepository userRepo,
            MovieRepository movieRepo
    ) {
        this.watchlistRepo = watchlistRepo;
        this.userRepo     = userRepo;
        this.movieRepo    = movieRepo;
    }

    @Override
    public WatchlistItem addToWatchlist(Long userId, Long movieId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        Movie movie = movieRepo.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found: " + movieId));

        // if already exists, return it (no duplicate)
        return watchlistRepo.findByUserIdAndMovieId(userId, movieId)
                .orElseGet(() -> watchlistRepo.save(new WatchlistItem(user, movie)));
    }

    @Override
    public void removeFromWatchlist(Long userId, Long movieId) {
        // deleteByUserIdAndMovieId does nothing if no match
        watchlistRepo.deleteByUserIdAndMovieId(userId, movieId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WatchlistItem> getWatchlist(Long userId, Pageable pageable) {
        if (!userRepo.existsById(userId)) {
            throw new ResourceNotFoundException("User not found: " + userId);
        }
        return watchlistRepo.findByUserId(userId, pageable);
    }
}
