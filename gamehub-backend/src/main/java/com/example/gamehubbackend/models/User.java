package com.example.gamehubbackend.models;

import com.example.gamehubbackend.dto.LibraryGameDTO;
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
   List<LibraryGameDTO> gameLibrary,
   LocalDateTime creationDate,
   LocalDateTime lastUpdateDate
) {}
