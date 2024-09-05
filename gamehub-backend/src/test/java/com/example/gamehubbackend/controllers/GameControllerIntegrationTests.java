package com.example.gamehubbackend.controllers;

import com.example.gamehubbackend.models.Game;
import com.example.gamehubbackend.repositories.GameRepository;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
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

    private static MockWebServer mockWebServer;

    @DynamicPropertySource
    static void backendProperties(DynamicPropertyRegistry registry) {
        registry.add("basic.url", () -> mockWebServer.url("/").toString());
        registry.add("RAWG_API_KEY", () -> "testkey");
    }

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

    }
    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getAllGames_When_DbEmpty_ReturnsEmptyList() throws Exception {

        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

    }

    @Test
    @DirtiesContext
    void getAllGames_When_DbNotEmpty_ReturnsGames() throws Exception {

        gameRepository.save(new Game("1","Super Mario World", List.of("Jump and Run"), "2024-08-26", List.of("NES","SNES"),  "linkToImg"));

        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                            [
                                              {
                                                  "id": "1",
                                                  "title": "Super Mario World",
                                                  "genre": ["Jump and Run"],
                                                  "releaseDate": "2024-08-26",
                                                  "platforms": ["NES","SNES"],
                                                  "coverImage": "linkToImg"
                                              }
                                            ]
                                          """)
                );
    }

    @Test
    @DirtiesContext
    void getGameById_Test() throws Exception {
        gameRepository.save(new Game("1","Super Mario World", List.of("Jump and Run"), "2024-08-26", List.of("NES","SNES"), "linkToImg"));

        mockMvc.perform(get("/api/games/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                            {
                                              "id": "1",
                                              "title": "Super Mario World",
                                              "genre": ["Jump and Run"],
                                              "releaseDate": "2024-08-26",
                                              "platforms": ["NES","SNES"],
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
                            "genre": ["RPG"],
                            "releaseDate": "2017-03-03",
                            "platforms": ["PC","PS3"],
                            "coverImage": "linkToImg"
                        }
                        """)
        )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                    {
                                        "title": "Test",
                                        "genre": ["RPG"],
                                        "releaseDate": "2017-03-03",
                                        "platforms": ["PC","PS3"],
                                        "coverImage": "linkToImg"
                                    }
                                    """)
                )
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DirtiesContext
    void updateGame_Test() throws Exception {
        gameRepository.save(new Game("1","Super Mario World", List.of("Jump and Run"), "2017-03-03", List.of("NES","SNES"), "linkToImg"));

        mockMvc.perform(put("/api/games/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                         {
                          "title": "Super Mario World 2",
                          "genre": ["Jump and Run"],
                          "releaseDate": "2017-03-03",
                          "platforms": ["NES","SNES"],
                          "coverImage": "linkToImg"
                         }
                         """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(content().json("""
                                             {
                                              "id": "1",
                                              "title": "Super Mario World 2",
                                              "genre": ["Jump and Run"],
                                              "releaseDate": "2017-03-03",
                                              "platforms": ["NES","SNES"],
                                              "coverImage": "linkToImg"
                                             }
                                           """));
    }

    @Test
    @DirtiesContext
    void deleteGame_Test() throws Exception {
        gameRepository.save(new Game("1","Super Mario World", List.of("Jump and Run"), "2017-03-03", List.of("NES","SNES"),"linkToImg"));

        mockMvc.perform(delete("/api/games/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                             []
                                           """)
                );
    }

    // EXTERNAL API TEST

    @Test
    void loadAllGames_Test() throws Exception {


        mockWebServer.enqueue(new MockResponse()
                .setBody(
                    """
                          {
                                   "count": 872869,
                                   "next": "https://api.rawg.io/api/games?key=41d8b6d27b964744895f044800cbc204&page=2",
                                   "previous": null,
                                   "results": [
                                     {
                                       "id": 3498,
                                       "slug": "grand-theft-auto-v",
                                       "name": "Grand Theft Auto V",
                                       "released": "2013-09-17",
                                       "tba": false,
                                       "background_image": "https://media.rawg.io/media/games/20a/20aa03a10cda45239fe22d035c0ebe64.jpg",
                                       "rating": 4.47,
                                       "rating_top": 5,
                                       "ratings": [
                                         {
                                           "id": 5,
                                           "title": "exceptional",
                                           "count": 4121,
                                           "percent": 59.12
                                         }
                                       ],
                                       "ratings_count": 6864,
                                       "reviews_text_count": 62,
                                       "added": 21137,
                                       "added_by_status": {
                                         "yet": 533,
                                         "owned": 12169,
                                         "beaten": 5986,
                                         "toplay": 610,
                                         "dropped": 1109,
                                         "playing": 730
                                       },
                                       "metacritic": 92,
                                       "playtime": 74,
                                       "suggestions_count": 434,
                                       "updated": "2024-09-04T13:53:37",
                                       "user_game": null,
                                       "reviews_count": 6970,
                                       "saturated_color": "0f0f0f",
                                       "dominant_color": "0f0f0f",
                                       "platforms": [
                                         {
                                           "platform": {
                                             "id": 4,
                                             "name": "PC",
                                             "slug": "pc",
                                             "image": null,
                                             "year_end": null,
                                             "year_start": null,
                                             "games_count": 535509,
                                             "image_background": "https://media.rawg.io/media/games/bc0/bc06a29ceac58652b684deefe7d56099.jpg"
                                           },
                                           "released_at": "2013-09-17",
                                           "requirements_en": {
                                             "minimum": "Minimum:OS: Windows 10 64 Bit, Windows 8.1 64 Bit, Windows 8 64 Bit, Windows 7 64 Bit Service Pack 1, Windows Vista 64 Bit Service Pack 2* (*NVIDIA video card recommended if running Vista OS)Processor: Intel Core 2 Quad CPU Q6600 @ 2.40GHz (4 CPUs) / AMD Phenom 9850 Quad-Core Processor (4 CPUs) @ 2.5GHzMemory: 4 GB RAMGraphics: NVIDIA 9800 GT 1GB / AMD HD 4870 1GB (DX 10, 10.1, 11)Storage: 72 GB available spaceSound Card: 100% DirectX 10 compatibleAdditional Notes: Over time downloadable content and programming changes will change the system requirements for this game. Please refer to your hardware manufacturer and www.rockstargames.com/support for current compatibility information. Some system components such as mobile chipsets, integrated, and AGP graphics cards may be incompatible. Unlisted specifications may not be supported by publisher. Other requirements: Installation and online play requires log-in to Rockstar Games Social Club (13+) network; internet connection required for activation, online play, and periodic entitlement verification; software installations required including Rockstar Games Social Club platform, DirectX, Chromium, and Microsoft Visual C++ 2008 sp1 Redistributable Package, and authentication software that recognizes certain hardware attributes for entitlement, digital rights management, system, and other support purposes. SINGLE USE SERIAL CODE REGISTRATION VIA INTERNET REQUIRED; REGISTRATION IS LIMITED TO ONE ROCKSTAR GAMES SOCIAL CLUB ACCOUNT (13+) PER SERIAL CODE; ONLY ONE PC LOG-IN ALLOWED PER SOCIAL CLUB ACCOUNT AT ANY TIME; SERIAL CODE(S) ARE NON-TRANSFERABLE ONCE USED; SOCIAL CLUB ACCOUNTS ARE NON-TRANSFERABLE. Partner Requirements: Please check the terms of service of this site before purchasing this software.",
                                             "recommended": "Recommended:OS: Windows 10 64 Bit, Windows 8.1 64 Bit, Windows 8 64 Bit, Windows 7 64 Bit Service Pack 1Processor: Intel Core i5 3470 @ 3.2GHz (4 CPUs) / AMD X8 FX-8350 @ 4GHz (8 CPUs)Memory: 8 GB RAMGraphics: NVIDIA GTX 660 2GB / AMD HD 7870 2GBStorage: 72 GB available spaceSound Card: 100% DirectX 10 compatibleAdditional Notes:"
                                           },
                                           "requirements_ru": null
                                         }
                                       ],
                                       "parent_platforms": [
                                         {
                                           "platform": {
                                             "id": 1,
                                             "name": "PC",
                                             "slug": "pc"
                                           }
                                         }
                                       ],
                                       "genres": [
                                         {
                                           "id": 4,
                                           "name": "Action",
                                           "slug": "action",
                                           "games_count": 182068,
                                           "image_background": "https://media.rawg.io/media/games/b7d/b7d3f1715fa8381a4e780173a197a615.jpg"
                                         }
                                       ],
                                       "stores": [
                                         {
                                           "id": 290375,
                                           "store": {
                                             "id": 3,
                                             "name": "PlayStation Store",
                                             "slug": "playstation-store",
                                             "domain": "store.playstation.com",
                                             "games_count": 7963,
                                             "image_background": "https://media.rawg.io/media/games/960/960b601d9541cec776c5fa42a00bf6c4.jpg"
                                           }
                                         }
                                       ],
                                       "clip": null,
                                       "tags": [
                                         {
                                           "id": 31,
                                           "name": "Singleplayer",
                                           "slug": "singleplayer",
                                           "language": "eng",
                                           "games_count": 227300,
                                           "image_background": "https://media.rawg.io/media/games/490/49016e06ae2103881ff6373248843069.jpg"
                                         }
                                       ],
                                       "esrb_rating": {
                                         "id": 4,
                                         "name": "Mature",
                                         "slug": "mature"
                                       },
                                       "short_screenshots": [
                                         {
                                           "id": -1,
                                           "image": "https://media.rawg.io/media/games/20a/20aa03a10cda45239fe22d035c0ebe64.jpg"
                                         }
                                       ]
                                     }
                                   ]
                                 }
                    """
                )
                .addHeader("Content-Type", "application/json")
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/games/fetch"))
                        .andExpect(status().isOk())
                        .andExpect(content().json(
                                """
                                    {
                                        "next": "https://api.rawg.io/api/games?key=41d8b6d27b964744895f044800cbc204&page=2",
                                            "previous": null,
                                            "games": [
                                                {
                                                    "id": "3498",
                                                    "title": "Grand Theft Auto V",
                                                    "genre": [
                                                        "Action"
                                                    ],
                                                    "releaseDate": "2013-09-17",
                                                    "platforms": [
                                                        "PC"
                                                    ],
                                                    "coverImage": "https://media.rawg.io/media/games/20a/20aa03a10cda45239fe22d035c0ebe64.jpg"
                                                }
                                            ]
                                    }
                                """
                            ));

    }
}