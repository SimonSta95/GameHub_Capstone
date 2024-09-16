package com.example.gamehubbackend.services.rawg;

import com.example.gamehubbackend.models.Game;
import com.example.gamehubbackend.models.rawg.RawgGameDetail;
import com.example.gamehubbackend.models.rawg.RawgGameList;
import com.example.gamehubbackend.models.rawg.RawgGameResponse;
import com.example.gamehubbackend.models.rawg.RawgGenre;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Service
public class RawgService {

    private final RestClient restClient;

    @Value("${rawg.api.key}")  // Injects the RAWG API key from application properties
    private String apiKey;

    /**
     * Constructs a RawgService with the specified base URL for the REST client.
     *
     * @param basicUrl The base URL for the REST client.
     */
    public RawgService(@Value("${basic.url}") String basicUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(basicUrl)  // Sets the base URL for the REST client
                .build();
    }

    /**
     * Loads a list of games from the RAWG API, optionally filtered by a search term.
     * The result is cached to improve performance.
     *
     * @param page  The page number to retrieve (defaults to 1 if null).
     * @param search The search term to filter games (null or empty means no filter).
     * @return A RawgGameList object containing the list of games and pagination details.
     */
    @Cacheable("games")  // Caches the result of this method to avoid repeated API calls
    public RawgGameList loadAllGames(String page, String search) {
        // Build the URI for the API request
        String uri = "api/games?page_size=40&page=" + (page != null ? page : 1) +
                "&key=" + apiKey;

        // Add search parameter if provided
        if (search != null && !search.trim().isEmpty()) {
            uri += "&search=" + URLEncoder.encode(search, StandardCharsets.UTF_8);
        }

        // Make the API request and retrieve the response
        RawgGameResponse body = restClient.get()
                .uri(uri)
                .retrieve()
                .body(RawgGameResponse.class);

        // Return an empty list if the response body is null
        if (body == null) {
            return new RawgGameList(0, null, null, Collections.emptyList());
        }

        // Map the RAWG game response to a list of Game objects
        return new RawgGameList(
                body.count(),
                body.next(),
                body.previous(),
                body.results()
                        .stream()
                        .map(game -> new Game(
                                String.valueOf(game.id()),
                                game.name(),
                                game.genres().stream()
                                        .map(RawgGenre::name)  // Map RAWG genres to their names
                                        .toList(),
                                game.released(),  // Release date
                                game.platforms().stream()
                                        .map(wrapper -> wrapper.platform().name())  // Map RAWG platforms to their names
                                        .toList(),
                                game.background_image()
                        ))
                        .toList());
    }

    /**
     * Loads detailed information about a specific game from the RAWG API.
     * The result is cached to improve performance.
     *
     * @param gameId The ID of the game to retrieve.
     * @return A RawgGameDetail object containing detailed information about the game.
     */
    @Cacheable("gameDetail")  // Caches the result of this method to avoid repeated API calls
    public RawgGameDetail loadGameDetail(String gameId) {
        // Make the API request and retrieve the game details
        return restClient.get()
                .uri("/api/games/" + gameId + "?key=" + apiKey)
                .retrieve()
                .body(RawgGameDetail.class);
    }
}
