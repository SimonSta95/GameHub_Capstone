package com.example.gamehubbackend.services;

import com.example.gamehubbackend.models.Game;
import com.example.gamehubbackend.exceptions.GameNotFoundException;
import lombok.RequiredArgsConstructor;
import com.example.gamehubbackend.models.GameDTO;
import org.springframework.stereotype.Service;
import com.example.gamehubbackend.repositories.GameRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final IdService idService;

    public List<Game> getAllGames() {

        return gameRepository.findAll();
    }

    public List<Game> getAllGamesById(List<String> gameIds) {
        return gameRepository.findAllById(gameIds);
    }

    public Game getGameById(String id) {

        return gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException("No Game found with id: " + id));
    }

    public Game addGame(GameDTO gameDTO) {

        Game gameToSave = new Game(
                idService.randomId(),
                gameDTO.title(),
                gameDTO.genre(),
                gameDTO.releaseDate(),
                gameDTO.platforms(),
                gameDTO.developer(),
                gameDTO.publisher(),
                gameDTO.description(),
                gameDTO.coverImage()
        );
        return gameRepository.save(gameToSave);
    }

    public Game updateGame(GameDTO updateGame, String id) {

        Game game = gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException("No Game found with id: " + id))
                .withTitle(updateGame.title())
                .withGenre(updateGame.genre())
                .withReleaseDate(updateGame.releaseDate())
                .withPlatforms(updateGame.platforms())
                .withDeveloper(updateGame.developer())
                .withPublisher(updateGame.publisher())
                .withDescription(updateGame.description())
                .withCoverImage(updateGame.coverImage());

        return gameRepository.save(game);
    }

    public void deleteGame(String id) {
        gameRepository.deleteById(id);
    }
}
