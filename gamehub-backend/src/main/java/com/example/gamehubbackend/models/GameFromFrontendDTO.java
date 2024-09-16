package com.example.gamehubbackend.models;

import java.util.List;

public record GameFromFrontendDTO(
        String id,
        String title,
        List<String> platforms,
        String coverImage
) {
}
