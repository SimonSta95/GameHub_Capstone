package com.example.gamehubbackend.dto;

public record AddGameDTO(
        String userId,
        LibraryGameDTO game
) {
}
