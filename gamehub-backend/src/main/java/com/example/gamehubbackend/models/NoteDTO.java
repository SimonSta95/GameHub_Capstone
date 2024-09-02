package com.example.gamehubbackend.models;

import java.time.LocalDateTime;

public record NoteDTO(
        String userId,
        String gameId,
        String title,
        String content,
        String category,
        LocalDateTime created,
        LocalDateTime updated
) {
}
