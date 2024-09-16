package com.example.gamehubbackend.services;

import com.example.gamehubbackend.models.Game;
import com.example.gamehubbackend.exceptions.GameNotFoundException;
import lombok.RequiredArgsConstructor;
import com.example.gamehubbackend.models.GameDTO;
import org.springframework.stereotype.Service;
import com.example.gamehubbackend.repositories.GameRepository;

import java.util.List;

@Service
@RequiredArgsConstructor  // Automatically injects final dependencies
public class GameService {

    private final GameRepository gameRepository;  // Repository for game data operations
    private final IdService idService;  // Service for generating unique IDs

    /**
     * Get all games.
     *
     * @return List of all games
     */
    public List<Game> getAllGames() {
        return gameRepository.findAll();  // Retrieves all games from the repository
    }

    /**
     * Get a game by its ID.
     *
     * @param id the ID of the game to retrieve
     * @return the Game object if found
     * @throws GameNotFoundException if the game with the given ID is not found
     */
    public Game getGameById(String id) {
        return gameRepository.findById(id)  // Search for the game by ID
                .orElseThrow(() -> new GameNotFoundException("No Game found with id: " + id));  // Throw exception if not found
    }

    /**
     * Add a new game.
     *
     * @param gameDTO the data transfer object containing game details
     * @return the saved Game object
     */
    public Game addGame(GameDTO gameDTO) {
        Game gameToSave = new Game(
                idService.randomId(),
                gameDTO.title(),
                gameDTO.genre(),
                gameDTO.releaseDate(),
                gameDTO.platforms(),
                gameDTO.coverImage()
        );
        return gameRepository.save(gameToSave);
    }

    /**
     * Update an existing game.
     *
     * @param updateGame the data transfer object with updated game details
     * @param id         the ID of the game to be updated
     * @return the updated Game object
     * @throws GameNotFoundException if the game with the given ID is not found
     */
    public Game updateGame(GameDTO updateGame, String id) {
        Game game = gameRepository.findById(id)  // Find the game by ID
                .orElseThrow(() -> new GameNotFoundException("No Game found with id: " + id))
                .withTitle(updateGame.title())
                .withGenre(updateGame.genre())
                .withReleaseDate(updateGame.releaseDate())
                .withPlatforms(updateGame.platforms())
                .withCoverImage(updateGame.coverImage());

        return gameRepository.save(game);
    }

    /**
     * Delete a game by its ID.
     *
     * @param id the ID of the game to delete
     */
    public void deleteGame(String id) {
        gameRepository.deleteById(id);
    }
}
