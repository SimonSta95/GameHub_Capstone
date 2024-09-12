package com.example.gamehubbackend.controllers;

import com.example.gamehubbackend.models.Game;
import com.example.gamehubbackend.models.rawg.RawgGameDetail;
import com.example.gamehubbackend.models.rawg.RawgGameList;
import com.example.gamehubbackend.services.GameService;
import com.example.gamehubbackend.services.rawg.RawgService;
import lombok.RequiredArgsConstructor;
import com.example.gamehubbackend.models.GameDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;  // Service for handling game operations
    private final RawgService rawgService;  // Service for interacting with external RAWG API

    // MongoDB Endpoints

    /**
     * Retrieve a list of all games from the database.
     *
     * @return a list of Game objects
     */
    @GetMapping
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }

    /**
     * Retrieve a specific game by its ID.
     *
     * @param id the ID of the game
     * @return the Game object with the specified ID
     */
    @GetMapping("/{id}")
    public Game getGameById(@PathVariable String id) {
        return gameService.getGameById(id);
    }

    /**
     * Add a new game to the database.
     *
     * @param game the GameDTO object containing game details
     * @return the newly added Game object
     */
    @PostMapping
    public Game addGame(@RequestBody GameDTO game) {
        return gameService.addGame(game);
    }

    /**
     * Update an existing game in the database.
     *
     * @param id the ID of the game to update
     * @param game the GameDTO object containing updated game details
     * @return the updated Game object
     */
    @PutMapping(path = {"{id}/update", "{id}"})
    public Game updateGame(@PathVariable String id, @RequestBody GameDTO game) {
        return gameService.updateGame(game, id);
    }

    /**
     * Delete a game from the database.
     *
     * @param id the ID of the game to delete
     */
    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable String id) {
        gameService.deleteGame(id);
    }

    // External API Endpoints

    /**
     * Fetch a list of games from the external RAWG API.
     *
     * @param page optional pagination parameter
     * @param search optional search query
     * @return a RawgGameList containing games from the RAWG API
     */
    @GetMapping("/fetch")
    public RawgGameList loadAllGames(
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String search) {
        return rawgService.loadAllGames(page, search);
    }

    /**
     * Fetch details of a specific game from the external RAWG API.
     *
     * @param id the ID of the game to fetch
     * @return a RawgGameDetail containing detailed information about the game
     */
    @GetMapping("/fetch/{id}")
    public RawgGameDetail loadGameById(@PathVariable String id) {
        return rawgService.loadGameDetail(id);
    }
}
