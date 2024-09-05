package com.example.gamehubbackend.models.rawg;

import java.util.List;

public record RawgGame(
        int id,
        String name,
        String released,
        String background_image,
        List<RawgGenre> genres,
        List<RawgPlatformWrapper> platforms
) {
}
