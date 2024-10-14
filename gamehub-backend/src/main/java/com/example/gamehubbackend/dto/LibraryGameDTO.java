package com.example.gamehubbackend.dto;

import java.util.List;

public record LibraryGameDTO(
        String id,
        String title,
        List<String> platforms,
        String coverImage
) {
}
