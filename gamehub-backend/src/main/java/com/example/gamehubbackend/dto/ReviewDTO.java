package com.example.gamehubbackend.dto;


public record ReviewDTO(
        String gameTitle,
        String userId,
        String gameId,
        String username,
        double rating,
        String content,
        String date
) {}
