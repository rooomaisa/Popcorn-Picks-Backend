package com.popcornpicks.controllers;

import com.popcornpicks.dto.PageResponse;
import com.popcornpicks.dto.WatchlistResponse;
import com.popcornpicks.mapper.WatchlistMapper;
import com.popcornpicks.models.WatchlistItem;
import com.popcornpicks.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/watchlist")
public class WatchlistController {

    private final WatchlistService service;
    private final WatchlistMapper mapper;

    @Autowired
    public WatchlistController(WatchlistService service, WatchlistMapper mapper) {
        this.service = service;
        this.mapper  = mapper;
    }


    @PostMapping
    public ResponseEntity<WatchlistResponse> add(
            @PathVariable Long userId,
            @RequestParam Long movieId
    ) {
        WatchlistItem item = service.addToWatchlist(userId, movieId);
        return new ResponseEntity<>(mapper.toDto(item), HttpStatus.CREATED);
    }


    @DeleteMapping("/{movieId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(
            @PathVariable Long userId,
            @PathVariable Long movieId
    ) {
        service.removeFromWatchlist(userId, movieId);
    }


    @GetMapping
    public PageResponse<WatchlistResponse> list(
            @PathVariable Long userId,
            Pageable pageable
    ) {
        Page<WatchlistItem> page = service.getWatchlist(userId, pageable);
        List<WatchlistResponse> dtos = page.getContent()
                .stream()
                .map(mapper::toDto)
                .toList();

        return new PageResponse<>(
                dtos,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
    }


