package com.example.gamehubbackend.controllers;

import com.example.gamehubbackend.dto.LibraryGameDTO;
import com.example.gamehubbackend.models.User;
import com.example.gamehubbackend.dto.UserDTO;
import com.example.gamehubbackend.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
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

    @Autowired
    ObjectMapper objectMapper;

    private final LocalDateTime localDate = LocalDateTime.parse("2024-08-14T00:00:00");
    private final LocalDateTime createdDate = LocalDateTime.parse("2024-08-22T00:00:00");

    @Test
    @DirtiesContext
    @WithMockUser(username = "TestUser", authorities = {"USER"})
    void getLoggedInUser() throws Exception {
        LibraryGameDTO game1 = new LibraryGameDTO("game1", "Game 1", List.of("Platform1"), "coverImage1");
        LibraryGameDTO game2 = new LibraryGameDTO("game2", "Game 2", List.of("Platform2"), "coverImage2");

        userRepository.save(new User("1", "TestUser", "Test", "githubId123", "avatarLink", "USER", List.of(game1, game2), localDate, createdDate));

        mockMvc.perform(get("/api/auth/me")
                        .with(oidcLogin().idToken(token -> token.subject("1"))
                                .userInfoToken(token -> token.claim("login", "TestUser"))))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                        {
                                          "id": "1",
                                          "username": "TestUser",
                                          "githubId": "githubId123",
                                          "avatarUrl": "avatarLink",
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
                                      """));
    }

    @Test
    @DirtiesContext
    void registerUser() throws Exception {
        UserDTO userDTO = new UserDTO(
                "NewUser",
                "password",
                "githubId123",
                "avatarUrl123",
                "USER",
                List.of(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("NewUser"))
                .andExpect(jsonPath("$.githubId").value("githubId123")); // Adjust to match the field gameTitle
    }

    @Test
    void loginEndpoint() throws Exception {
        mockMvc.perform(post("/api/auth/login"))
                .andExpect(status().isOk());
    }
}