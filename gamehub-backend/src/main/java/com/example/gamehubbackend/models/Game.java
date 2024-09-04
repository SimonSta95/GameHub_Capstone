package com.example.gamehubbackend.models;

import lombok.With;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@With
@Document("games")
public record Game(
        String id,
        String title,
        String genre,
        LocalDate releaseDate,
        List<String> platforms,
        String description,
        String coverImage
) {}