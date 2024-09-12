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
import org.springframework.security.test.context.support.WithMockUser;
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
    @WithMockUser(username = "TestUser", roles = {"USER"})
    void getAllGames_When_DbEmpty_ReturnsEmptyList() throws Exception {

        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "TestUser", roles = {"USER"})
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
    @WithMockUser(username = "TestUser", roles = {"USER"})
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
    @WithMockUser(username = "TestUser", roles = {"USER"})
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
    @WithMockUser(username = "TestUser", roles = {"USER"})
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
    @WithMockUser(username = "TestUser", roles = {"USER"})
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
    @WithMockUser(username = "TestUser", roles = {"USER"})
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
    @WithMockUser(username = "TestUser", roles = {"USER"})
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

    @Test
    @WithMockUser(username = "TestUser", roles = {"USER"})
    void loadGameDetails_test() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setBody(
                        """
                                {
                                    "id": 3498,
                                    "slug": "grand-theft-auto-v",
                                    "name": "Grand Theft Auto V",
                                    "name_original": "Grand Theft Auto V",
                                    "description": "<p>Rockstar Games went bigger, since their previous installment of the series. You get the complicated and realistic world-building from Liberty City of GTA4 in the setting of lively and diverse Los Santos, from an old fan favorite GTA San Andreas. 561 different vehicles (including every transport you can operate) and the amount is rising with every update. <br />\\nSimultaneous storytelling from three unique perspectives: <br />\\nFollow Michael, ex-criminal living his life of leisure away from the past, Franklin, a kid that seeks the better future, and Trevor, the exact past Michael is trying to run away from. <br />\\nGTA Online will provide a lot of additional challenge even for the experienced players, coming fresh from the story mode. Now you will have other players around that can help you just as likely as ruin your mission. Every GTA mechanic up to date can be experienced by players through the unique customizable character, and community content paired with the leveling system tends to keep everyone busy and engaged.</p>\\n<p>Español<br />\\nRockstar Games se hizo más grande desde su entrega anterior de la serie. Obtienes la construcción del mundo complicada y realista de Liberty City de GTA4 en el escenario de Los Santos, un viejo favorito de los fans, GTA San Andreas. 561 vehículos diferentes (incluidos todos los transportes que puede operar) y la cantidad aumenta con cada actualización.<br />\\nNarración simultánea desde tres perspectivas únicas:<br />\\nSigue a Michael, ex-criminal que vive su vida de ocio lejos del pasado, Franklin, un niño que busca un futuro mejor, y Trevor, el pasado exacto del que Michael está tratando de huir.<br />\\nGTA Online proporcionará muchos desafíos adicionales incluso para los jugadores experimentados, recién llegados del modo historia. Ahora tendrás otros jugadores cerca que pueden ayudarte con la misma probabilidad que arruinar tu misión. Los jugadores pueden experimentar todas las mecánicas de GTA actualizadas a través del personaje personalizable único, y el contenido de la comunidad combinado con el sistema de nivelación tiende a mantener a todos ocupados y comprometidos.</p>",
                                    "metacritic": 92,
                                    "metacritic_platforms": [
                                        {
                                            "metascore": 96,
                                            "url": "https://www.metacritic.com/game/pc/grand-theft-auto-v",
                                            "platform": {
                                                "platform": 4,
                                                "name": "PC",
                                                "slug": "pc"
                                            }
                                        },
                                        {
                                            "metascore": 97,
                                            "url": "https://www.metacritic.com/game/playstation-3/grand-theft-auto-v",
                                            "platform": {
                                                "platform": 16,
                                                "name": "PlayStation 3",
                                                "slug": "playstation3"
                                            }
                                        },
                                        {
                                            "metascore": 97,
                                            "url": "https://www.metacritic.com/game/playstation-4/grand-theft-auto-v",
                                            "platform": {
                                                "platform": 18,
                                                "name": "PlayStation 4",
                                                "slug": "playstation4"
                                            }
                                        },
                                        {
                                            "metascore": 81,
                                            "url": "https://www.metacritic.com/game/playstation-5/grand-theft-auto-v",
                                            "platform": {
                                                "platform": 187,
                                                "name": "PlayStation 5",
                                                "slug": "playstation5"
                                            }
                                        },
                                        {
                                            "metascore": 97,
                                            "url": "https://www.metacritic.com/game/xbox-360/grand-theft-auto-v",
                                            "platform": {
                                                "platform": 14,
                                                "name": "Xbox 360",
                                                "slug": "xbox360"
                                            }
                                        },
                                        {
                                            "metascore": 97,
                                            "url": "https://www.metacritic.com/game/xbox-one/grand-theft-auto-v",
                                            "platform": {
                                                "platform": 1,
                                                "name": "Xbox One",
                                                "slug": "xbox-one"
                                            }
                                        },
                                        {
                                            "metascore": 79,
                                            "url": "https://www.metacritic.com/game/xbox-series-x/grand-theft-auto-v",
                                            "platform": {
                                                "platform": 186,
                                                "name": "Xbox Series S/X",
                                                "slug": "xbox-series-x"
                                            }
                                        }
                                    ],
                                    "released": "2013-09-17",
                                    "tba": false,
                                    "updated": "2024-09-04T13:53:37",
                                    "background_image": "https://media.rawg.io/media/games/20a/20aa03a10cda45239fe22d035c0ebe64.jpg",
                                    "background_image_additional": "https://media.rawg.io/media/screenshots/5f5/5f5a38a222252d996b18962806eed707.jpg",
                                    "website": "http://www.rockstargames.com/V/",
                                    "rating": 4.47,
                                    "rating_top": 5,
                                    "ratings": [
                                        {
                                            "id": 5,
                                            "title": "exceptional",
                                            "count": 4121,
                                            "percent": 59.12
                                        },
                                        {
                                            "id": 4,
                                            "title": "recommended",
                                            "count": 2278,
                                            "percent": 32.68
                                        },
                                        {
                                            "id": 3,
                                            "title": "meh",
                                            "count": 442,
                                            "percent": 6.34
                                        },
                                        {
                                            "id": 1,
                                            "title": "skip",
                                            "count": 129,
                                            "percent": 1.85
                                        }
                                    ],
                                    "reactions": {
                                        "1": 30,
                                        "2": 8,
                                        "3": 37,
                                        "4": 23,
                                        "5": 13,
                                        "6": 10,
                                        "7": 19,
                                        "8": 23,
                                        "9": 2,
                                        "10": 10,
                                        "11": 20,
                                        "12": 16,
                                        "13": 1,
                                        "14": 3,
                                        "15": 2,
                                        "16": 6,
                                        "18": 4,
                                        "20": 1,
                                        "21": 2
                                    },
                                    "added": 21138,
                                    "added_by_status": {
                                        "yet": 533,
                                        "owned": 12169,
                                        "beaten": 5987,
                                        "toplay": 610,
                                        "dropped": 1109,
                                        "playing": 730
                                    },
                                    "playtime": 74,
                                    "screenshots_count": 58,
                                    "movies_count": 8,
                                    "creators_count": 11,
                                    "achievements_count": 539,
                                    "parent_achievements_count": 75,
                                    "reddit_url": "https://www.reddit.com/r/GrandTheftAutoV/",
                                    "reddit_name": "",
                                    "reddit_description": "",
                                    "reddit_logo": "",
                                    "reddit_count": 5184,
                                    "twitch_count": 102,
                                    "youtube_count": 1000000,
                                    "reviews_text_count": 106,
                                    "ratings_count": 6864,
                                    "suggestions_count": 434,
                                    "alternative_names": [
                                        "GTA 5",
                                        "GTA V",
                                        "GTA5",
                                        "GTAV"
                                    ],
                                    "metacritic_url": "https://www.metacritic.com/game/pc/grand-theft-auto-v",
                                    "parents_count": 0,
                                    "additions_count": 3,
                                    "game_series_count": 12,
                                    "user_game": null,
                                    "reviews_count": 6970,
                                    "saturated_color": "0f0f0f",
                                    "dominant_color": "0f0f0f",
                                    "parent_platforms": [
                                        {
                                            "platform": {
                                                "id": 1,
                                                "name": "PC",
                                                "slug": "pc"
                                            }
                                        },
                                        {
                                            "platform": {
                                                "id": 2,
                                                "name": "PlayStation",
                                                "slug": "playstation"
                                            }
                                        },
                                        {
                                            "platform": {
                                                "id": 3,
                                                "name": "Xbox",
                                                "slug": "xbox"
                                            }
                                        }
                                    ],
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
                                            "requirements": {
                                                "minimum": "Minimum:OS: Windows 10 64 Bit, Windows 8.1 64 Bit, Windows 8 64 Bit, Windows 7 64 Bit Service Pack 1, Windows Vista 64 Bit Service Pack 2* (*NVIDIA video card recommended if running Vista OS)Processor: Intel Core 2 Quad CPU Q6600 @ 2.40GHz (4 CPUs) / AMD Phenom 9850 Quad-Core Processor (4 CPUs) @ 2.5GHzMemory: 4 GB RAMGraphics: NVIDIA 9800 GT 1GB / AMD HD 4870 1GB (DX 10, 10.1, 11)Storage: 72 GB available spaceSound Card: 100% DirectX 10 compatibleAdditional Notes: Over time downloadable content and programming changes will change the system requirements for this game.  Please refer to your hardware manufacturer and www.rockstargames.com/support for current compatibility information. Some system components such as mobile chipsets, integrated, and AGP graphics cards may be incompatible. Unlisted specifications may not be supported by publisher.     Other requirements:  Installation and online play requires log-in to Rockstar Games Social Club (13+) network; internet connection required for activation, online play, and periodic entitlement verification; software installations required including Rockstar Games Social Club platform, DirectX , Chromium, and Microsoft Visual C++ 2008 sp1 Redistributable Package, and authentication software that recognizes certain hardware attributes for entitlement, digital rights management, system, and other support purposes.     SINGLE USE SERIAL CODE REGISTRATION VIA INTERNET REQUIRED; REGISTRATION IS LIMITED TO ONE ROCKSTAR GAMES SOCIAL CLUB ACCOUNT (13+) PER SERIAL CODE; ONLY ONE PC LOG-IN ALLOWED PER SOCIAL CLUB ACCOUNT AT ANY TIME; SERIAL CODE(S) ARE NON-TRANSFERABLE ONCE USED; SOCIAL CLUB ACCOUNTS ARE NON-TRANSFERABLE.  Partner Requirements:  Please check the terms of service of this site before purchasing this software.",
                                                "recommended": "Recommended:OS: Windows 10 64 Bit, Windows 8.1 64 Bit, Windows 8 64 Bit, Windows 7 64 Bit Service Pack 1Processor: Intel Core i5 3470 @ 3.2GHz (4 CPUs) / AMD X8 FX-8350 @ 4GHz (8 CPUs)Memory: 8 GB RAMGraphics: NVIDIA GTX 660 2GB / AMD HD 7870 2GBStorage: 72 GB available spaceSound Card: 100% DirectX 10 compatibleAdditional Notes:"
                                            }
                                        },
                                        {
                                            "platform": {
                                                "id": 187,
                                                "name": "PlayStation 5",
                                                "slug": "playstation5",
                                                "image": null,
                                                "year_end": null,
                                                "year_start": 2020,
                                                "games_count": 1113,
                                                "image_background": "https://media.rawg.io/media/games/f24/f2493ea338fe7bd3c7d73750a85a0959.jpeg"
                                            },
                                            "released_at": "2013-09-17",
                                            "requirements": {}
                                        },
                                        {
                                            "platform": {
                                                "id": 186,
                                                "name": "Xbox Series S/X",
                                                "slug": "xbox-series-x",
                                                "image": null,
                                                "year_end": null,
                                                "year_start": 2020,
                                                "games_count": 978,
                                                "image_background": "https://media.rawg.io/media/games/34b/34b1f1850a1c06fd971bc6ab3ac0ce0e.jpg"
                                            },
                                            "released_at": "2013-09-17",
                                            "requirements": {}
                                        },
                                        {
                                            "platform": {
                                                "id": 18,
                                                "name": "PlayStation 4",
                                                "slug": "playstation4",
                                                "image": null,
                                                "year_end": null,
                                                "year_start": null,
                                                "games_count": 6829,
                                                "image_background": "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg"
                                            },
                                            "released_at": "2013-09-17",
                                            "requirements": {}
                                        },
                                        {
                                            "platform": {
                                                "id": 16,
                                                "name": "PlayStation 3",
                                                "slug": "playstation3",
                                                "image": null,
                                                "year_end": null,
                                                "year_start": null,
                                                "games_count": 3166,
                                                "image_background": "https://media.rawg.io/media/games/021/021c4e21a1824d2526f925eff6324653.jpg"
                                            },
                                            "released_at": "2013-09-17",
                                            "requirements": {}
                                        },
                                        {
                                            "platform": {
                                                "id": 14,
                                                "name": "Xbox 360",
                                                "slug": "xbox360",
                                                "image": null,
                                                "year_end": null,
                                                "year_start": null,
                                                "games_count": 2806,
                                                "image_background": "https://media.rawg.io/media/games/157/15742f2f67eacff546738e1ab5c19d20.jpg"
                                            },
                                            "released_at": "2013-09-17",
                                            "requirements": {}
                                        },
                                        {
                                            "platform": {
                                                "id": 1,
                                                "name": "Xbox One",
                                                "slug": "xbox-one",
                                                "image": null,
                                                "year_end": null,
                                                "year_start": null,
                                                "games_count": 5641,
                                                "image_background": "https://media.rawg.io/media/games/157/15742f2f67eacff546738e1ab5c19d20.jpg"
                                            },
                                            "released_at": "2013-09-17",
                                            "requirements": {}
                                        }
                                    ],
                                    "stores": [
                                        {
                                            "id": 290375,
                                            "url": "",
                                            "store": {
                                                "id": 3,
                                                "name": "PlayStation Store",
                                                "slug": "playstation-store",
                                                "domain": "store.playstation.com",
                                                "games_count": 7963,
                                                "image_background": "https://media.rawg.io/media/games/960/960b601d9541cec776c5fa42a00bf6c4.jpg"
                                            }
                                        },
                                        {
                                            "id": 438095,
                                            "url": "",
                                            "store": {
                                                "id": 11,
                                                "name": "Epic Games",
                                                "slug": "epic-games",
                                                "domain": "epicgames.com",
                                                "games_count": 1345,
                                                "image_background": "https://media.rawg.io/media/games/942/9424d6bb763dc38d9378b488603c87fa.jpg"
                                            }
                                        },
                                        {
                                            "id": 290376,
                                            "url": "",
                                            "store": {
                                                "id": 1,
                                                "name": "Steam",
                                                "slug": "steam",
                                                "domain": "store.steampowered.com",
                                                "games_count": 98563,
                                                "image_background": "https://media.rawg.io/media/games/587/587588c64afbff80e6f444eb2e46f9da.jpg"
                                            }
                                        },
                                        {
                                            "id": 290377,
                                            "url": "",
                                            "store": {
                                                "id": 7,
                                                "name": "Xbox 360 Store",
                                                "slug": "xbox360",
                                                "domain": "marketplace.xbox.com",
                                                "games_count": 1916,
                                                "image_background": "https://media.rawg.io/media/games/960/960b601d9541cec776c5fa42a00bf6c4.jpg"
                                            }
                                        },
                                        {
                                            "id": 290378,
                                            "url": "",
                                            "store": {
                                                "id": 2,
                                                "name": "Xbox Store",
                                                "slug": "xbox-store",
                                                "domain": "microsoft.com",
                                                "games_count": 4868,
                                                "image_background": "https://media.rawg.io/media/games/157/15742f2f67eacff546738e1ab5c19d20.jpg"
                                            }
                                        }
                                    ],
                                    "developers": [
                                        {
                                            "id": 3524,
                                            "name": "Rockstar North",
                                            "slug": "rockstar-north",
                                            "games_count": 28,
                                            "image_background": "https://media.rawg.io/media/screenshots/ff8/ff85fc7832ddca3c39f3539389793ee6.jpg"
                                        },
                                        {
                                            "id": 10,
                                            "name": "Rockstar Games",
                                            "slug": "rockstar-games",
                                            "games_count": 25,
                                            "image_background": "https://media.rawg.io/media/screenshots/d74/d748bb016b1c26935cb7c2d0f2a7ba40.jpg"
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
                                    "tags": [
                                        {
                                            "id": 31,
                                            "name": "Singleplayer",
                                            "slug": "singleplayer",
                                            "language": "eng",
                                            "games_count": 227300,
                                            "image_background": "https://media.rawg.io/media/games/490/49016e06ae2103881ff6373248843069.jpg"
                                        },
                                        {
                                            "id": 40847,
                                            "name": "Steam Achievements",
                                            "slug": "steam-achievements",
                                            "language": "eng",
                                            "games_count": 39573,
                                            "image_background": "https://media.rawg.io/media/games/7cf/7cfc9220b401b7a300e409e539c9afd5.jpg"
                                        },
                                        {
                                            "id": 7,
                                            "name": "Multiplayer",
                                            "slug": "multiplayer",
                                            "language": "eng",
                                            "games_count": 38617,
                                            "image_background": "https://media.rawg.io/media/games/960/960b601d9541cec776c5fa42a00bf6c4.jpg"
                                        },
                                        {
                                            "id": 40836,
                                            "name": "Full controller support",
                                            "slug": "full-controller-support",
                                            "language": "eng",
                                            "games_count": 18795,
                                            "image_background": "https://media.rawg.io/media/games/8e4/8e4de3f54ac659e08a7ba6a2b731682a.jpg"
                                        },
                                        {
                                            "id": 13,
                                            "name": "Atmospheric",
                                            "slug": "atmospheric",
                                            "language": "eng",
                                            "games_count": 34121,
                                            "image_background": "https://media.rawg.io/media/games/2ba/2bac0e87cf45e5b508f227d281c9252a.jpg"
                                        },
                                        {
                                            "id": 42,
                                            "name": "Great Soundtrack",
                                            "slug": "great-soundtrack",
                                            "language": "eng",
                                            "games_count": 3406,
                                            "image_background": "https://media.rawg.io/media/games/8a0/8a02f84a5916ede2f923b88d5f8217ba.jpg"
                                        },
                                        {
                                            "id": 24,
                                            "name": "RPG",
                                            "slug": "rpg",
                                            "language": "eng",
                                            "games_count": 21772,
                                            "image_background": "https://media.rawg.io/media/games/713/713269608dc8f2f40f5a670a14b2de94.jpg"
                                        },
                                        {
                                            "id": 18,
                                            "name": "Co-op",
                                            "slug": "co-op",
                                            "language": "eng",
                                            "games_count": 11956,
                                            "image_background": "https://media.rawg.io/media/games/c6b/c6bfece1daf8d06bc0a60632ac78e5bf.jpg"
                                        },
                                        {
                                            "id": 36,
                                            "name": "Open World",
                                            "slug": "open-world",
                                            "language": "eng",
                                            "games_count": 7733,
                                            "image_background": "https://media.rawg.io/media/games/16b/16b1b7b36e2042d1128d5a3e852b3b2f.jpg"
                                        },
                                        {
                                            "id": 411,
                                            "name": "cooperative",
                                            "slug": "cooperative",
                                            "language": "eng",
                                            "games_count": 5181,
                                            "image_background": "https://media.rawg.io/media/games/0bd/0bd5646a3d8ee0ac3314bced91ea306d.jpg"
                                        },
                                        {
                                            "id": 8,
                                            "name": "First-Person",
                                            "slug": "first-person",
                                            "language": "eng",
                                            "games_count": 32415,
                                            "image_background": "https://media.rawg.io/media/games/00d/00d374f12a3ab5f96c500a2cfa901e15.jpg"
                                        },
                                        {
                                            "id": 149,
                                            "name": "Third Person",
                                            "slug": "third-person",
                                            "language": "eng",
                                            "games_count": 11948,
                                            "image_background": "https://media.rawg.io/media/games/da1/da1b267764d77221f07a4386b6548e5a.jpg"
                                        },
                                        {
                                            "id": 4,
                                            "name": "Funny",
                                            "slug": "funny",
                                            "language": "eng",
                                            "games_count": 25381,
                                            "image_background": "https://media.rawg.io/media/games/e3d/e3ddc524c6292a435d01d97cc5f42ea7.jpg"
                                        },
                                        {
                                            "id": 37,
                                            "name": "Sandbox",
                                            "slug": "sandbox",
                                            "language": "eng",
                                            "games_count": 7134,
                                            "image_background": "https://media.rawg.io/media/games/310/3106b0e012271c5ffb16497b070be739.jpg"
                                        },
                                        {
                                            "id": 123,
                                            "name": "Comedy",
                                            "slug": "comedy",
                                            "language": "eng",
                                            "games_count": 12776,
                                            "image_background": "https://media.rawg.io/media/games/49c/49c3dfa4ce2f6f140cc4825868e858cb.jpg"
                                        },
                                        {
                                            "id": 150,
                                            "name": "Third-Person Shooter",
                                            "slug": "third-person-shooter",
                                            "language": "eng",
                                            "games_count": 3524,
                                            "image_background": "https://media.rawg.io/media/games/16b/16b1b7b36e2042d1128d5a3e852b3b2f.jpg"
                                        },
                                        {
                                            "id": 62,
                                            "name": "Moddable",
                                            "slug": "moddable",
                                            "language": "eng",
                                            "games_count": 952,
                                            "image_background": "https://media.rawg.io/media/games/62c/62c7c8b28a27b83680b22fb9d33fc619.jpg"
                                        },
                                        {
                                            "id": 144,
                                            "name": "Crime",
                                            "slug": "crime",
                                            "language": "eng",
                                            "games_count": 2894,
                                            "image_background": "https://media.rawg.io/media/games/20a/20aa03a10cda45239fe22d035c0ebe64.jpg"
                                        },
                                        {
                                            "id": 62349,
                                            "name": "vr mod",
                                            "slug": "vr-mod",
                                            "language": "eng",
                                            "games_count": 17,
                                            "image_background": "https://media.rawg.io/media/screenshots/1bb/1bb3f78f0fe43b5d5ca2f3da5b638840.jpg"
                                        }
                                    ],
                                    "publishers": [
                                        {
                                            "id": 2155,
                                            "name": "Rockstar Games",
                                            "slug": "rockstar-games",
                                            "games_count": 79,
                                            "image_background": "https://media.rawg.io/media/games/682/682973f711e9ea6fcf11f71cbb39cdd5.jpeg"
                                        }
                                    ],
                                    "esrb_rating": {
                                        "id": 4,
                                        "name": "Mature",
                                        "slug": "mature"
                                    },
                                    "clip": null,
                                    "description_raw": "Rockstar Games went bigger, since their previous installment of the series. You get the complicated and realistic world-building from Liberty City of GTA4 in the setting of lively and diverse Los Santos, from an old fan favorite GTA San Andreas. 561 different vehicles (including every transport you can operate) and the amount is rising with every update. \\nSimultaneous storytelling from three unique perspectives: \\nFollow Michael, ex-criminal living his life of leisure away from the past, Franklin, a kid that seeks the better future, and Trevor, the exact past Michael is trying to run away from. \\nGTA Online will provide a lot of additional challenge even for the experienced players, coming fresh from the story mode. Now you will have other players around that can help you just as likely as ruin your mission. Every GTA mechanic up to date can be experienced by players through the unique customizable character, and community content paired with the leveling system tends to keep everyone busy and engaged.\\n\\nEspañol\\nRockstar Games se hizo más grande desde su entrega anterior de la serie. Obtienes la construcción del mundo complicada y realista de Liberty City de GTA4 en el escenario de Los Santos, un viejo favorito de los fans, GTA San Andreas. 561 vehículos diferentes (incluidos todos los transportes que puede operar) y la cantidad aumenta con cada actualización.\\nNarración simultánea desde tres perspectivas únicas:\\nSigue a Michael, ex-criminal que vive su vida de ocio lejos del pasado, Franklin, un niño que busca un futuro mejor, y Trevor, el pasado exacto del que Michael está tratando de huir.\\nGTA Online proporcionará muchos desafíos adicionales incluso para los jugadores experimentados, recién llegados del modo historia. Ahora tendrás otros jugadores cerca que pueden ayudarte con la misma probabilidad que arruinar tu misión. Los jugadores pueden experimentar todas las mecánicas de GTA actualizadas a través del personaje personalizable único, y el contenido de la comunidad combinado con el sistema de nivelación tiende a mantener a todos ocupados y comprometidos."
                                }
                                """
                )
                .addHeader("Content-Type", "application/json")
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/games/fetch/3498"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                            {
                                 "id": 3498,
                                 "name": "Grand Theft Auto V",
                                 "description": "<p>Rockstar Games went bigger, since their previous installment of the series. You get the complicated and realistic world-building from Liberty City of GTA4 in the setting of lively and diverse Los Santos, from an old fan favorite GTA San Andreas. 561 different vehicles (including every transport you can operate) and the amount is rising with every update. <br />\\nSimultaneous storytelling from three unique perspectives: <br />\\nFollow Michael, ex-criminal living his life of leisure away from the past, Franklin, a kid that seeks the better future, and Trevor, the exact past Michael is trying to run away from. <br />\\nGTA Online will provide a lot of additional challenge even for the experienced players, coming fresh from the story mode. Now you will have other players around that can help you just as likely as ruin your mission. Every GTA mechanic up to date can be experienced by players through the unique customizable character, and community content paired with the leveling system tends to keep everyone busy and engaged.</p>\\n<p>Español<br />\\nRockstar Games se hizo más grande desde su entrega anterior de la serie. Obtienes la construcción del mundo complicada y realista de Liberty City de GTA4 en el escenario de Los Santos, un viejo favorito de los fans, GTA San Andreas. 561 vehículos diferentes (incluidos todos los transportes que puede operar) y la cantidad aumenta con cada actualización.<br />\\nNarración simultánea desde tres perspectivas únicas:<br />\\nSigue a Michael, ex-criminal que vive su vida de ocio lejos del pasado, Franklin, un niño que busca un futuro mejor, y Trevor, el pasado exacto del que Michael está tratando de huir.<br />\\nGTA Online proporcionará muchos desafíos adicionales incluso para los jugadores experimentados, recién llegados del modo historia. Ahora tendrás otros jugadores cerca que pueden ayudarte con la misma probabilidad que arruinar tu misión. Los jugadores pueden experimentar todas las mecánicas de GTA actualizadas a través del personaje personalizable único, y el contenido de la comunidad combinado con el sistema de nivelación tiende a mantener a todos ocupados y comprometidos.</p>",
                                 "released": "2013-09-17",
                                 "background_image": "https://media.rawg.io/media/games/20a/20aa03a10cda45239fe22d035c0ebe64.jpg",
                                 "platforms": [
                                     {
                                         "platform": {
                                             "name": "PC"
                                         }
                                     },
                                     {
                                         "platform": {
                                             "name": "PlayStation 5"
                                         }
                                     },
                                     {
                                         "platform": {
                                             "name": "Xbox Series S/X"
                                         }
                                     },
                                     {
                                         "platform": {
                                             "name": "PlayStation 4"
                                         }
                                     },
                                     {
                                         "platform": {
                                             "name": "PlayStation 3"
                                         }
                                     },
                                     {
                                         "platform": {
                                             "name": "Xbox 360"
                                         }
                                     },
                                     {
                                         "platform": {
                                             "name": "Xbox One"
                                         }
                                     }
                                 ],
                                 "developers": [
                                     {
                                         "name": "Rockstar North"
                                     },
                                     {
                                         "name": "Rockstar Games"
                                     }
                                 ],
                                 "genres": [
                                     {
                                         "name": "Action"
                                     }
                                 ],
                                 "publishers": [
                                     {
                                         "name": "Rockstar Games"
                                     }
                                 ]
                             }
                        """
                ));
    }
}