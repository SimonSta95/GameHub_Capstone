package com.example.gamehubbackend.models;

import java.util.List;

public record FrontendGame(
        String id,
        String title,
        List<String> platforms,
        String coverImage
) {
}
