package com.example.gamehubbackend.models;

public record AddGameDTO(
        String userId,
        FrontendGame game
) {
}
