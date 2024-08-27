package com.example.gamehubbackend.repositories;

import com.example.gamehubbackend.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByGitHubId(String gitHubId);
}
