package com.example.gamehubbackend.controllers;

import com.example.gamehubbackend.models.Review;
import com.example.gamehubbackend.repositories.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ReviewRepository reviewRepository;

    @Test
    @WithMockUser
    void getAllReviews() throws Exception {
        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @WithMockUser
    @DirtiesContext
    void getReviewsByGameId() throws Exception {
        reviewRepository.save(new Review("1", "Test","user1", "game1", "username1", 4.5, "Great game!", "2020-01-01"));

        mockMvc.perform(get("/api/reviews/game1"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                        [
                            {
                                "id": "1",
                                "gameTitle": "Test",
                                "userId": "user1",
                                "gameId": "game1",
                                "username": "username1",
                                "rating": 4.5,
                                "content": "Great game!",
                                "date": "2020-01-01"
                            }
                        ]
                        """
                ));
    }

    @Test
    @WithMockUser
    void getReviewsByGameId_whenNotFound() throws Exception {
        mockMvc.perform(get("/api/reviews/game1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @WithMockUser
    @DirtiesContext
    void addReview() throws Exception {
        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                {
                                  "userId": "user1",
                                  "gameId": "game1",
                                  "username": "username1",
                                  "rating": 4.5,
                                  "content": "Great game!",
                                  "date": "2020-01-01"
                                }
                                """
                        ))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/reviews/game1"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                        [
                            {
                                "userId": "user1",
                                "gameId": "game1",
                                "username": "username1",
                                "rating": 4.5,
                                "content": "Great game!",
                                "date": "2020-01-01"
                            }
                        ]
                        """
                ))
                .andExpect(jsonPath("$[0].id").exists());

    }

    @Test
    @WithMockUser
    @DirtiesContext
    void updateReview() throws Exception {
        reviewRepository.save(new Review("1","Test", "user1", "game1", "username1", 4.5, "Great game!", "2020-01-01"));

        mockMvc.perform(put("/api/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                {
                                  "gameTitle": "Test",
                                  "userId": "user1",
                                  "gameId": "game1",
                                  "username": "username1",
                                  "rating": 4.8,
                                  "content": "Amazing game!",
                                  "date": "2020-01-01"
                                }
                                """
                        ))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/reviews/game1"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                        [
                            {
                                "gameTitle": "Test",
                                "userId": "user1",
                                "gameId": "game1",
                                "username": "username1",
                                "rating": 4.8,
                                "content": "Amazing game!",
                                "date": "2020-01-01"
                            }
                        ]
                        """
                ));
    }

    @Test
    @WithMockUser
    @DirtiesContext
    void deleteReview() throws Exception {
        reviewRepository.save(new Review("1", "Test","user1", "game1", "username1", 4.5, "Great game!", "2020-01-01"));

        mockMvc.perform(delete("/api/reviews/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/reviews/game1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}