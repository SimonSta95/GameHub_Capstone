package com.example.gamehubbackend.controllers;

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

        userRepository.save(new User("1", "TestUser", "1","link", "USER", List.of("Game 1", "Game 2"), localDate, createdDate));

        mockMvc.perform(get("/api/auth/me")
                .with(oidcLogin().idToken(token-> token.subject("1"))
                        .userInfoToken(token -> token.claim("login", "TestUser"))))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                            {
                                              "id": "1",
                                              "username": "TestUser",
                                              "gitHubId": "1",
                                              "role": "USER",
                                              "gameLibrary": ["Game 1", "Game 2"]
                                            }
                                          """))
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$.lastUpdateDate").exists());


    }


}