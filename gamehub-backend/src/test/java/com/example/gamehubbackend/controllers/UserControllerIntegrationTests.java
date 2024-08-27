package com.example.gamehubbackend.controllers;

import com.example.gamehubbackend.models.User;
import com.example.gamehubbackend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    private final LocalDateTime localDateTime = LocalDateTime.parse("2020-01-01T01:00:00");
    private final LocalDateTime updateDateTime = LocalDateTime.parse("2020-01-01T02:00:00");

    @Test
    @WithMockUser
    void getAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @WithMockUser
    @DirtiesContext
    void getUserById() throws Exception {
        userRepository.save(new User("1", "TestUser1", "1", "USER", List.of("Game 1", "Game 2", "Game 3"), localDateTime, updateDateTime));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                    """
                    {
                      "id": "1",
                      "username": "TestUser1",
                      "gitHubId": "1",
                      "role": "USER",
                      "gameLibrary": ["Game 1", "Game 2", "Game 3"],
                      "creationDate": "2020-01-01T01:00:00",
                      "lastUpdateDate": "2020-01-01T02:00:00"
                    }
                """
                ));
    }

    @Test
    @WithMockUser
    void getUserById_whenUserNoFound() throws Exception {
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(
                    """
                        {
                          "message": "No user found with id: 1",
                          "statusCode": 404
                        }
                    """
                ));
    }

    @Test
    @WithMockUser
    @DirtiesContext
    void getUserByGitHubId() throws Exception {
        userRepository.save(new User("1", "TestUser1", "1", "USER", List.of("Game 1", "Game 2", "Game 3"), localDateTime, updateDateTime));

        mockMvc.perform(get("/api/users/g/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                        {
                          "id": "1",
                          "username": "TestUser1",
                          "gitHubId": "1",
                          "role": "USER",
                          "gameLibrary": ["Game 1", "Game 2", "Game 3"],
                          "creationDate": "2020-01-01T01:00:00",
                          "lastUpdateDate": "2020-01-01T02:00:00"
                        }
                    """
                ));
    }

    @Test
    @WithMockUser
    void getUserByGitHubId_whenUserNoFound() throws Exception {
        mockMvc.perform(get("/api/users/g/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(
                        """
                            {
                              "message": "No user found with gitHubId: 1",
                              "statusCode": 404
                            }
                        """
                ));
    }

    @Test
    @WithMockUser
    @DirtiesContext
    void addUser() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                  """
                  {
                    "username": "TestUser1",
                    "gitHubId": "1",
                    "role": "USER",
                    "gameLibrary": ["Game 1", "Game 2", "Game 3"],
                    "creationDate": "2020-01-01T01:00:00",
                    "lastUpdateDate": "2020-01-01T02:00:00"
                  }
                  """
                ))
                .andExpect(status().isOk())
                .andExpect(content().json(
                    """
                    {
                      "username": "TestUser1",
                      "gitHubId": "1",
                      "role": "USER",
                      "gameLibrary": ["Game 1", "Game 2", "Game 3"],
                      "creationDate": "2020-01-01T01:00:00",
                      "lastUpdateDate": "2020-01-01T02:00:00"
                    }
                    """
                ))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser
    @DirtiesContext
    void updateUser() throws Exception {

        userRepository.save(new User("1", "TestUser1", "1", "USER", List.of("Game 1", "Game 2", "Game 3"), localDateTime, updateDateTime));

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                        {
                            "username": "TestUser1",
                            "gitHubId": "1",
                            "role": "USER",
                            "gameLibrary": ["Game 1", "Game 2"],
                            "creationDate": "2020-01-01T01:00:00",
                            "lastUpdateDate": "2020-01-01T02:00:00"
                        }
                        """
                ))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                        {
                            "id": "1",
                            "username": "TestUser1",
                            "gitHubId": "1",
                            "role": "USER",
                            "gameLibrary": ["Game 1", "Game 2"],
                            "creationDate": "2020-01-01T01:00:00"
                        }
                        """
                )).andExpect(jsonPath("$.lastUpdateDate").exists());
    }

    @Test
    @WithMockUser
    @DirtiesContext
    void deleteUser() throws Exception {
        userRepository.save(new User("1", "TestUser1", "1", "USER", List.of("Game 1", "Game 2", "Game 3"), localDateTime, updateDateTime));

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

}
