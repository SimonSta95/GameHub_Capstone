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
   String password,
   String gitHubId,
   String avatarUrl,
   String role,
   List<GameFromFrontendDTO> gameLibrary,
   LocalDateTime creationDate,
   LocalDateTime lastUpdateDate
) {}
