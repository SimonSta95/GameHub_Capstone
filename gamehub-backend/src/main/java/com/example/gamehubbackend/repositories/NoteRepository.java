package com.example.gamehubbackend.repositories;

import com.example.gamehubbackend.models.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends MongoRepository<Note, String> {

    Optional<List<Note>> findByUserId(String userId);
}
