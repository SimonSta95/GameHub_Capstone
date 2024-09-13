package com.example.gamehubbackend.models;

import lombok.With;

import java.time.LocalDateTime;
import java.util.List;

@With
public record UserDTO(
        String username,
        String password,
        String gitHubId,
        String avatarUrl,
        String role,
        List<GameFromFrontendDTO> gameLibrary,
        LocalDateTime creationDate,
        LocalDateTime lastUpdateDate
) {}
