package com.example.gamehubbackend.models;

import java.time.LocalDate;
import java.util.List;

public record GameDTO(
        String title,
        String genre,
        LocalDate releaseDate,
        List<String> platforms,
        String description,
        String coverImage
) {}
