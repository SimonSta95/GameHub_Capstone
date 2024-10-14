package com.example.gamehubbackend.models;

import com.example.gamehubbackend.dto.LibraryGameDTO;
import lombok.With;

import java.util.List;

@With
public record UserResponse(
        String id,
        String githubId,
        String username,
        String avatarUrl,
        String role,
        List<LibraryGameDTO> gameLibrary
) {
}
