package com.example.gamehubbackend.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("reviews")
public record Review(
        String id,
        String userId,
        String gameId,
        String username,
        double rating,
        String content,
        String date
) {
}
