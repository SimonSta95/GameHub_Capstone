package com.example.gamehubbackend.models;

import lombok.With;

import java.util.List;

@With
public record UserResponse(
        String id,
        String githubId,
        String username,
        String avatarUrl,
        String role,
        List<GameFromFrontendDTO> gameLibrary
) {
}
