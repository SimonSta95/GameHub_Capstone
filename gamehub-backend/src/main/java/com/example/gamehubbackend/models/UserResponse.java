package com.example.gamehubbackend.models;

import java.util.List;

public record UserResponse(
        String id,
        String githubId,
        String username,
        String avatarUrl,
        String role,
        List<FrontendGame> gameLibrary
) {
}
