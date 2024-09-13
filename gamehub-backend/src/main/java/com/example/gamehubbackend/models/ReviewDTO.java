package com.example.gamehubbackend.models;


public record ReviewDTO(
        String name,
        String userId,
        String gameId,
        String username,
        double rating,
        String content,
        String date
) { }
