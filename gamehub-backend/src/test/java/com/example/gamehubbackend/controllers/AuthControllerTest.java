package com.example.gamehubbackend.controllers;

import com.example.gamehubbackend.models.FrontendGame;
import com.example.gamehubbackend.models.User;
import com.example.gamehubbackend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    private final LocalDateTime localDate = LocalDateTime.parse("2024-08-14T00:00:00");
    private final LocalDateTime createdDate = LocalDateTime.parse("2024-08-22T00:00:00");

    @Test
    @DirtiesContext
    void getLoggedInUser() throws Exception {
        // Create FrontendGame objects
        FrontendGame game1 = new FrontendGame("game1", "Game 1", List.of("Platform1"), "coverImage1");
        FrontendGame game2 = new FrontendGame("game2", "Game 2", List.of("Platform2"), "coverImage2");

        // Save user with FrontendGame objects in the gameLibrary
        userRepository.save(new User("1", "TestUser", "1", "link", "USER", List.of(game1, game2), localDate, createdDate));

        // Perform the request and validate the response
        mockMvc.perform(get("/api/auth/me")
                        .with(oidcLogin().idToken(token -> token.subject("1"))
                                .userInfoToken(token -> token.claim("login", "TestUser"))))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                            {
                                              "id": "1",
                                              "username": "TestUser",
                                              "gitHubId": "1",
                                              "role": "USER",
                                              "gameLibrary": [
                                                {
                                                  "id": "game1",
                                                  "title": "Game 1",
                                                  "platforms": ["Platform1"],
                                                  "coverImage": "coverImage1"
                                                },
                                                {
                                                  "id": "game2",
                                                  "title": "Game 2",
                                                  "platforms": ["Platform2"],
                                                  "coverImage": "coverImage2"
                                                }
                                              ]
                                            }
                                          """))
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$.lastUpdateDate").exists());
    }
}