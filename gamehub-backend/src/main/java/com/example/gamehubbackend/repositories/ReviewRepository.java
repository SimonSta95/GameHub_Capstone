package com.example.gamehubbackend.repositories;

import com.example.gamehubbackend.models.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findByGameId(String gameId);
}
