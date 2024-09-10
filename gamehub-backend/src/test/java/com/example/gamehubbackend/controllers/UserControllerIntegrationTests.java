package com.example.gamehubbackend.controllers;

import com.example.gamehubbackend.models.FrontendGame;
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
        FrontendGame game1 = new FrontendGame("game1", "Game 1", List.of("Platform1"), "coverImage1");
        FrontendGame game2 = new FrontendGame("game2", "Game 2", List.of("Platform2"), "coverImage2");
        FrontendGame game3 = new FrontendGame("game3", "Game 3", List.of("Platform3"), "coverImage3");

        userRepository.save(new User("1", "TestUser1", "Test","1", "link", "USER", List.of(game1, game2, game3), localDateTime, updateDateTime));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                        {
                          "id": "1",
                          "username": "TestUser1",
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
                            },
                            {
                              "id": "game3",
                              "title": "Game 3",
                              "platforms": ["Platform3"],
                              "coverImage": "coverImage3"
                            }
                          ],
                          "creationDate": "2020-01-01T01:00:00",
                          "lastUpdateDate": "2020-01-01T02:00:00"
                        }
                    """
                ));
    }

    @Test
    @WithMockUser
    @DirtiesContext
    void getUserByGitHubId() throws Exception {
        FrontendGame game1 = new FrontendGame("game1", "Game 1", List.of("Platform1"), "coverImage1");
        FrontendGame game2 = new FrontendGame("game2", "Game 2", List.of("Platform2"), "coverImage2");

        userRepository.save(new User("1", "TestUser1","Test", "1", "link", "USER", List.of(game1, game2), localDateTime, updateDateTime));

        mockMvc.perform(get("/api/users/g/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                        {
                          "id": "1",
                          "username": "TestUser1",
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
                          ],
                          "creationDate": "2020-01-01T01:00:00",
                          "lastUpdateDate": "2020-01-01T02:00:00"
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
                                  ],
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
                          ],
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
        FrontendGame game1 = new FrontendGame("game1", "Game 1", List.of("Platform1"), "coverImage1");
        FrontendGame game2 = new FrontendGame("game2", "Game 2", List.of("Platform2"), "coverImage2");

        userRepository.save(new User("1", "TestUser1","Test", "1", "link", "USER", List.of(game1, game2), localDateTime, updateDateTime));

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                {
                                    "username": "TestUser1",
                                    "gitHubId": "1",
                                    "role": "USER",
                                    "gameLibrary": [
                                      {
                                        "id": "game1",
                                        "title": "Game 1",
                                        "platforms": ["Platform1"],
                                        "coverImage": "coverImage1"
                                      }
                                    ],
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
                            "gameLibrary": [
                              {
                                "id": "game1",
                                "title": "Game 1",
                                "platforms": ["Platform1"],
                                "coverImage": "coverImage1"
                              }
                            ],
                            "creationDate": "2020-01-01T01:00:00"
                        }
                        """
                ))
                .andExpect(jsonPath("$.lastUpdateDate").exists());
    }

    @Test
    @WithMockUser
    @DirtiesContext
    void deleteUser() throws Exception {
        FrontendGame game1 = new FrontendGame("game1", "Game 1", List.of("Platform1"), "coverImage1");
        FrontendGame game2 = new FrontendGame("game2", "Game 2", List.of("Platform2"), "coverImage2");

        userRepository.save(new User("1", "TestUser1","Test", "1", "link", "USER", List.of(game1, game2), localDateTime, updateDateTime));

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @WithMockUser
    @DirtiesContext
    void addGameToLibrary() throws Exception {
        userRepository.save(new User("1", "TestUser1", "Test", "1", "link", "USER", List.of(), localDateTime, updateDateTime));

        mockMvc.perform(put("/api/users/addGame")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                {
                                    "userId": "1",
                                    "game": {
                                        "id": "game1",
                                        "title": "Game 1",
                                        "platforms": ["Platform1"],
                                        "coverImage": "coverImage1"
                                    }
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
                            "gameLibrary": [
                              {
                                "id": "game1",
                                "title": "Game 1",
                                "platforms": ["Platform1"],
                                "coverImage": "coverImage1"
                              }
                            ],
                            "creationDate": "2020-01-01T01:00:00"
                        }
                        """
                ));
    }

    @Test
    @WithMockUser
    @DirtiesContext
    void deleteGameFromLibrary() throws Exception {
        FrontendGame game1 = new FrontendGame("game1", "Game 1", List.of("Platform1"), "coverImage1");

        userRepository.save(new User("1", "TestUser1","Test", "1", "link", "USER", List.of(game1), localDateTime, updateDateTime));

        mockMvc.perform(put("/api/users/deleteGame")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                {
                                    "userId": "1",
                                    "game": {
                                        "id": "game1",
                                        "title": "Game 1",
                                        "genre": ["Genre1"],
                                        "releaseDate": "2024-01-01",
                                        "platforms": ["Platform1"],
                                        "coverImage": "coverImage1"
                                    }
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
                            "gameLibrary": [],
                            "creationDate": "2020-01-01T01:00:00"
                        }
                        """
                ));
    }

    @Test
    @WithMockUser(username = "TestUser1", authorities = "USER")
    void getLoggedInUser() throws Exception {
        FrontendGame game1 = new FrontendGame("game1", "Game 1", List.of("Platform1"), "coverImage1");

        User user = new User("1", "TestUser1", "Test", "1", "link", "USER", List.of(game1), localDateTime, updateDateTime);
        userRepository.save(user);

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                        {
                          "id": "1",
                          "username": "TestUser1",
                          "githubId": "1",
                          "role": "USER",
                          "gameLibrary": [
                            {
                              "id": "game1",
                              "title": "Game 1",
                              "platforms": ["Platform1"],
                              "coverImage": "coverImage1"
                            }
                          ]
                        }
                        """
                ));
    }

    @Test
    @WithMockUser
    void getUserByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void getUserByGitHubIdNotFound() throws Exception {
        mockMvc.perform(get("/api/users/g/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DirtiesContext
    void addDuplicateGameToLibrary() throws Exception {
        userRepository.save(new User("1", "TestUser1", "Test", "1", "link", "USER", List.of(), localDateTime, updateDateTime));

        // Add the game once
        mockMvc.perform(put("/api/users/addGame")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                {
                                    "userId": "1",
                                    "game": {
                                        "id": "game1",
                                        "title": "Game 1",
                                        "platforms": ["Platform1"],
                                        "coverImage": "coverImage1"
                                    }
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
                            "gameLibrary": [
                              {
                                "id": "game1",
                                "title": "Game 1",
                                "platforms": ["Platform1"],
                                "coverImage": "coverImage1"
                              }
                            ],
                            "creationDate": "2020-01-01T01:00:00"
                        }
                        """
                ));

        // Attempt to add the same game again
        mockMvc.perform(put("/api/users/addGame")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                {
                                    "userId": "1",
                                    "game": {
                                        "id": "game1",
                                        "title": "Game 1",
                                        "platforms": ["Platform1"],
                                        "coverImage": "coverImage1"
                                    }
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
                            "gameLibrary": [
                              {
                                "id": "game1",
                                "title": "Game 1",
                                "platforms": ["Platform1"],
                                "coverImage": "coverImage1"
                              }
                            ],
                            "creationDate": "2020-01-01T01:00:00"
                        }
                        """
                ));
    }

    @Test
    @WithMockUser
    @DirtiesContext
    void deleteNonExistentGameFromLibrary() throws Exception {
        FrontendGame game1 = new FrontendGame("game1", "Game 1", List.of("Platform1"), "coverImage1");

        userRepository.save(new User("1", "TestUser1", "Test", "1", "link", "USER", List.of(game1), localDateTime, updateDateTime));

        mockMvc.perform(put("/api/users/deleteGame")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                {
                                    "userId": "1",
                                    "game": {
                                        "id": "nonExistentGame",
                                        "title": "Non-Existent Game",
                                        "platforms": ["Platform1"],
                                        "coverImage": "coverImage1"
                                    }
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
                            "gameLibrary": [
                              {
                                "id": "game1",
                                "title": "Game 1",
                                "platforms": ["Platform1"],
                                "coverImage": "coverImage1"
                              }
                            ],
                            "creationDate": "2020-01-01T01:00:00"
                        }
                        """
                ));
    }

    @Test
    @WithMockUser
    void addUserWithInvalidData() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                {
                                  "username": "",
                                  "gitHubId": "1",
                                  "role": "INVALID_ROLE",
                                  "gameLibrary": [],
                                  "creationDate": "invalid-date",
                                  "lastUpdateDate": "invalid-date"
                                }
                                """
                        ))
                .andExpect(status().isBadRequest());
    }

}
