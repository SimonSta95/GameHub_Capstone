package com.example.gamehubbackend.models;

import java.util.List;

public record GameDTO(
        String title,
        List<String> genre,
        String releaseDate,
        List<String> platforms,
        //String description,
        String coverImage
) {}
