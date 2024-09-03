package com.example.gamehubbackend.repositories;

import com.example.gamehubbackend.models.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoteRepository extends MongoRepository<Note, String> {
}
