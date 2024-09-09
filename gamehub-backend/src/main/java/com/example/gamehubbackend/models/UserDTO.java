package com.example.gamehubbackend.models;

import lombok.With;

import java.time.LocalDateTime;
import java.util.List;

@With
public record UserDTO(
        String username,
        String gitHubId,
        String avatarUrl,
        String role,
        List<FrontendGame> gameLibrary,
        LocalDateTime creationDate,
        LocalDateTime lastUpdateDate
) {}
