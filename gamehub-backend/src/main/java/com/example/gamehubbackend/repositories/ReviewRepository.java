package com.example.gamehubbackend.repositories;

import com.example.gamehubbackend.models.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, String> {

    Optional<List<Review>> findByGameId(String gameId);
    Optional<List<Review>> findByUserId(String userId);
}
