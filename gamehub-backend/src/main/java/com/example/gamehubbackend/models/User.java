package com.example.gamehubbackend.models;

import lombok.With;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@With
@Document("users")
public record User(
   String id,
   String username,
   String gitHubId,
   String role,
   List<String> gameLibrary,
   LocalDateTime creationDate,
   LocalDateTime lastUpdateDate
) {}
