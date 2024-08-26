package com.example.gamehubbackend.repositories;

import com.example.gamehubbackend.models.Game;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepository extends MongoRepository<Game, String> {
}
