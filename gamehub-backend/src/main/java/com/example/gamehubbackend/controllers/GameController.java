package com.example.gamehubbackend.controllers;

import com.example.gamehubbackend.models.Game;
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

    private final GameService gameService;
    private final RawgService rawgService;


    // MONGO DB ENDPOINTS

    @GetMapping
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }

    @GetMapping("/{id}")
    public Game getGameById(@PathVariable String id) {
        return gameService.getGameById(id);
    }

    @PostMapping
    public Game addGame(@RequestBody GameDTO game) {
        return gameService.addGame(game);
    }

    @PutMapping(path = {"{id}/update", "{id}"})
    public Game updateGame(@PathVariable String id, @RequestBody GameDTO game) {
        return gameService.updateGame(game, id);
    }

    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable String id) {
        gameService.deleteGame(id);
    }

    // EXTERNAL API ENDPOINTS

    @GetMapping("/fetch")
    public RawgGameList loadAllGames() {
        return rawgService.loadAllGames();
    }
}
