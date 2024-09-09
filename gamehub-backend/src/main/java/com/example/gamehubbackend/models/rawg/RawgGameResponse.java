package com.example.gamehubbackend.models.rawg;

import java.util.List;

public record RawgGameResponse(
        int count,
        String next,
        String previous,
        List<RawgGame> results
) {
}
