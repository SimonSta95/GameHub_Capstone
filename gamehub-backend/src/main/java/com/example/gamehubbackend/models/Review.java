package com.example.gamehubbackend.models;

import lombok.With;
import org.springframework.data.mongodb.core.mapping.Document;

@With
@Document("reviews")
public record Review(
        String id,
        String gameTitle,
        String userId,
        String gameId,
        String username,
        double rating,
        String content,
        String date
) {}
