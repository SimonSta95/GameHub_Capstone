package com.example.gamehubbackend.controllers;

import com.example.gamehubbackend.models.Note;
import com.example.gamehubbackend.repositories.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class NoteControllerIntegrationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    NoteRepository noteRepository;

    private final LocalDateTime createdDateTime = LocalDateTime.parse("2020-01-01T01:00:00");
    private final LocalDateTime updatedDateTime = LocalDateTime.parse("2020-01-01T02:00:00");

    @BeforeEach
    public void setUp() {
        // Set up a mock user directly in the SecurityContext
        UserDetails user = User.withUsername("user1")
                .password("password") // can be any value since it's mocked
                .roles("USER")
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void getAllNotes() throws Exception {
        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DirtiesContext
    void getNoteById() throws Exception {
        noteRepository.save(new Note("1", "Test","user1", "game1", "Title 1", "Content 1", "Category 1", createdDateTime, updatedDateTime));

        mockMvc.perform(get("/api/notes/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                        {
                          "id": "1",
                          "userId": "user1",
                          "gameId": "game1",
                          "title": "Title 1",
                          "content": "Content 1",
                          "category": "Category 1",
                          "created": "2020-01-01T01:00:00",
                          "updated": "2020-01-01T02:00:00"
                        }
                        """
                ));
    }

    @Test
    void getNoteById_whenNoteNotFound() throws Exception {
        mockMvc.perform(get("/api/notes/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(
                        """
                        {
                          "message": "No note found with id: 1",
                          "statusCode": 404
                        }
                        """
                ));
    }

    @Test
    @DirtiesContext
    void addNote() throws Exception {
        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                {
                                  "userId": "user1",
                                  "gameId": "game1",
                                  "title": "New Note",
                                  "content": "Note content",
                                  "category": "General",
                                  "created": "2020-01-01T01:00:00",
                                  "updated": "2020-01-01T02:00:00"
                                }
                                """
                        ))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                        {
                          "userId": "user1",
                          "gameId": "game1",
                          "title": "New Note",
                          "content": "Note content",
                          "category": "General"
                        }
                        """
                ))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.created").isNotEmpty())
                .andExpect(jsonPath("$.updated").isNotEmpty());
    }

//    @Test
//    @DirtiesContext
//    void updateNote() throws Exception {
//        noteRepository.save(new Note("1", "Test","user1", "game1", "Title 1", "Content 1", "Category 1", createdDateTime, updatedDateTime));
//
//        mockMvc.perform(put("/api/notes/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(
//                                """
//                                {
//                                  "userId": "user1",
//                                  "gameId": "game1",
//                                  "title": "Updated Title",
//                                  "content": "Updated content",
//                                  "category": "General",
//                                  "created": "2020-01-01T01:00:00"
//                                }
//                                """
//                        ))
//                .andExpect(status().isOk())
//                .andExpect(content().json(
//                        """
//                        {
//                          "id": "1",
//                          "userId": "user1",
//                          "gameId": "game1",
//                          "title": "Updated Title",
//                          "content": "Updated content",
//                          "category": "General"
//                        }
//                        """
//                ))
//                .andExpect(jsonPath("$.created").isNotEmpty())
//                .andExpect(jsonPath("$.updated").isNotEmpty());
//    }

//    @Test
//    @DirtiesContext
//    void deleteNote() throws Exception {
//        noteRepository.save(new Note("1", "Test","user1", "game1", "Title 1", "Content 1", "Category 1", createdDateTime, updatedDateTime));
//
//        mockMvc.perform(delete("/api/notes/1"))
//                .andExpect(status().isNoContent());
//
//        mockMvc.perform(get("/api/notes"))
//                .andExpect(status().isOk())
//                .andExpect(content().json("[]"));
//    }
}
