package com.example.gamehubbackend.dto;

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
        List<LibraryGameDTO> gameLibrary,
        LocalDateTime creationDate,
        LocalDateTime lastUpdateDate
) {}
