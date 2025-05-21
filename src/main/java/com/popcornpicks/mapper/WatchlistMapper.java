package com.popcornpicks.mapper;

import com.popcornpicks.dto.WatchlistResponse;
import com.popcornpicks.models.WatchlistItem;
import org.springframework.stereotype.Component;

@Component
public class WatchlistMapper {

    public WatchlistResponse toDto(WatchlistItem item) {
        return new WatchlistResponse(
                item.getId(),
                item.getMovie().getId(),
                item.getMovie().getTitle(),
                item.getAddedAt()
        );
    }
}
