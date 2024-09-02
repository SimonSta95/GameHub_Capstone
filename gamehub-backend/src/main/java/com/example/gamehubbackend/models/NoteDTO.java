package com.example.gamehubbackend.models;

public record NoteDTO(
        String userId,
        String gameId,
        String title,
        String content,
        String category
) {
}
