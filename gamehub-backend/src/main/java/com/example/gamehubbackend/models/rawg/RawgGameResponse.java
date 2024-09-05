package com.example.gamehubbackend.models.rawg;

import java.util.List;

public record RawgGameResponse(
        String next,
        String previous,
        List<RawgGame> results
) {
}
