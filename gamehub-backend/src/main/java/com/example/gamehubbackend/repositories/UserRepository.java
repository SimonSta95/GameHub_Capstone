package com.example.gamehubbackend.repositories;

import com.example.gamehubbackend.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
