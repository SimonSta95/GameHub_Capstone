package com.example.gamehubbackend.models;

import lombok.With;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@With
@Document("games")
public record Game(
        String id,
        String title,
        List<String> genre,
        String releaseDate,
        List<String> platforms,
        //String description,
        String coverImage
) {}