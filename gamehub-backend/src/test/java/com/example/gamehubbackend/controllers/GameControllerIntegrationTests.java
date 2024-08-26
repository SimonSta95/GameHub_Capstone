package com.example.gamehubbackend.controllers;

import com.example.gamehubbackend.models.Game;
import com.example.gamehubbackend.repositories.GameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class GameControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    GameRepository gameRepository;

    private final LocalDate localDate = LocalDate.parse("2024-08-26");

    @Test
    void getAllGames_When_DbEmpty_ReturnsEmptyList() throws Exception {

        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

    }

    @Test
    @DirtiesContext
    void getAllGames_When_DbNotEmpty_ReturnsGames() throws Exception {

        gameRepository.save(new Game("1","Super Mario World", "Jump and Run", localDate, List.of("NES","SNES"), "Nintendo", "Nintendo", "test123", "linkToImg"));

        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                            [
                                              {
                                                  "id": "1",
                                                  "title": "Super Mario World",
                                                  "genre": "Jump and Run",
                                                  "releaseDate": "2024-08-26",
                                                  "platforms": ["NES","SNES"],
                                                  "developer": "Nintendo",
                                                  "publisher": "Nintendo",
                                                  "description": "test123",
                                                  "coverImage": "linkToImg"
                                              }
                                            ]
                                          """)
                );
    }

    @Test
    @DirtiesContext
    void getGameById_Test() throws Exception {
        gameRepository.save(new Game("1","Super Mario World", "Jump and Run", localDate, List.of("NES","SNES"), "Nintendo", "Nintendo", "test123", "linkToImg"));

        mockMvc.perform(get("/api/games/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                            {
                                              "id": "1",
                                              "title": "Super Mario World",
                                              "genre": "Jump and Run",
                                              "releaseDate": "2024-08-26",
                                              "platforms": ["NES","SNES"],
                                              "developer": "Nintendo",
                                              "publisher": "Nintendo",
                                              "description": "test123",
                                              "coverImage": "linkToImg"
                                            }
                                          """)
                );
    }

    @Test
    @DirtiesContext
    void getGameById_Test_IdNotFound() throws Exception {

        mockMvc.perform(get("/api/games/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("""
                                              {
                                                "message": "No Game found with id: 1"
                                              }
                                          """)
                );
    }

    @Test
    @DirtiesContext
    void addGame_Test() throws Exception {

        mockMvc.perform(post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title": "Test",
                            "genre": "RPG",
                            "releaseDate": "2017-03-03",
                            "platforms": ["PC","PS3"],
                            "developer": "me",
                            "publisher": "me",
                            "description": "test123",
                            "coverImage": "linkToImg"
                        }
                        """)
        )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                    {
                                        "title": "Test",
                                        "genre": "RPG",
                                        "releaseDate": "2017-03-03",
                                        "platforms": ["PC","PS3"],
                                        "developer": "me",
                                        "publisher": "me",
                                        "description": "test123",
                                        "coverImage": "linkToImg"
                                    }
                                    """)
                )
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DirtiesContext
    void updateGame_Test() throws Exception {
        gameRepository.save(new Game("1","Super Mario World", "Jump and Run", localDate, List.of("NES","SNES"), "Nintendo", "Nintendo", "test123", "linkToImg"));

        mockMvc.perform(put("/api/games/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                         {
                          "title": "Super Mario World 2",
                          "genre": "Jump and Run",
                          "releaseDate": "2017-03-03",
                          "platforms": ["NES","SNES"],
                          "developer": "Nintendo",
                          "publisher": "Nintendo",
                          "description": "test123",
                          "coverImage": "linkToImg"
                         }
                         """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(content().json("""
                                             {
                                              "id": "1",
                                              "title": "Super Mario World 2",
                                              "genre": "Jump and Run",
                                              "releaseDate": "2017-03-03",
                                              "platforms": ["NES","SNES"],
                                              "developer": "Nintendo",
                                              "publisher": "Nintendo",
                                              "description": "test123",
                                              "coverImage": "linkToImg"
                                             }
                                           """));
    }

    @Test
    @DirtiesContext
    void deleteGame_Test() throws Exception {
        gameRepository.save(new Game("1","Super Mario World", "Jump and Run", localDate, List.of("NES","SNES"), "Nintendo", "Nintendo", "test123", "linkToImg"));

        mockMvc.perform(delete("/api/games/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                             []
                                           """)
                );
    }
}