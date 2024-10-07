package com.example.gamehubbackend.dto;

import java.time.LocalDateTime;

public record NoteDTO(
        String userId,
        String gameTitle,
        String gameId,
        String title,
        String content,
        String category,
        LocalDateTime created,
        LocalDateTime updated
) {}
