package com.example.gamehubbackend.models.rawg;

import java.util.List;

public record RawgGameDetail(
        int id,
        String name,
        String description,
        String released,
        String background_image,
        List<RawgPlatformWrapper> platforms,
        List<RawgDeveloper> developers,
        List<RawgGenre>genres,
        List<RawgPublisher> publishers
) {
}
