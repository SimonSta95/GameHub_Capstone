package com.example.gamehubbackend.models;

import lombok.With;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@With
@Document("notes")
public record Note(
        String id,
        String gameTitle,
        String userId,
        String gameId,
        String title,
        String content,
        String category,
        LocalDateTime created,
        LocalDateTime updated
) {}
