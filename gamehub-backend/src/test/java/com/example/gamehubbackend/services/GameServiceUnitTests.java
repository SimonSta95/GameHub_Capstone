package com.example.gamehubbackend.services;

import com.example.gamehubbackend.exceptions.GameNotFoundException;
import com.example.gamehubbackend.models.Game;
import com.example.gamehubbackend.models.GameDTO;
import com.example.gamehubbackend.repositories.GameRepository;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceUnitTests {
    private final GameRepository gameRepo = mock(GameRepository.class);
    private final IdService idService = mock(IdService.class);
    private final GameService gameService = new GameService(gameRepo, idService);

    @Test
    void getAllGames_Test() {
        List<Game> allGames = List.of(
                new Game("1", "Super Mario World", List.of("Jump and Run"), "2020-01-01", List.of("NES", "SNES"), "linkToImg"),
                new Game("2", "Donkey Kong", List.of("Jump and Run"), "2020-01-01", List.of("NES", "SNES"), "linkToImg")
        );

        List<Game> expectedGames = List.of(
                new Game("1", "Super Mario World", List.of("Jump and Run"), "2020-01-01", List.of("NES", "SNES"), "linkToImg"),
                new Game("2", "Donkey Kong", List.of("Jump and Run"), "2020-01-01", List.of("NES", "SNES"), "linkToImg")
        );

        when(gameRepo.findAll()).thenReturn(allGames);
        List<Game> actualGames = gameService.getAllGames();

        verify(gameRepo).findAll();
        assertEquals(expectedGames, actualGames);
    }

    @Test
    void getAllGames_WhenEmpty_ReturnsEmptyList() {
        List<Game> allGames = List.of();
        when(gameRepo.findAll()).thenReturn(allGames);
        List<Game> actualGames = gameService.getAllGames();

        assertEquals(allGames, actualGames);
    }

    @Test
    void getGameById_Test() {
        Game game = new Game("1", "Super Mario World", List.of("Jump and Run"), "2020-01-01", List.of("NES", "SNES"), "linkToImg");
        when(gameRepo.findById("1")).thenReturn(Optional.of(game));

        Game actualGame = gameService.getGameById("1");

        Game expectedGame = new Game("1", "Super Mario World", List.of("Jump and Run"), "2020-01-01", List.of("NES", "SNES"), "linkToImg");
        verify(gameRepo).findById("1");
        assertEquals(expectedGame, actualGame);
    }

    @Test
    void getGame_Test_whenGameDoesNotExist_ThenThrowException() {
        when(gameRepo.findById("1")).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () -> gameService.getGameById("1"));
        verify(gameRepo).findById("1");
    }

    @Test
    void addGame_Test_whenGameAsInput_thenReturnNewGame() {
        GameDTO gameDTO = new GameDTO("Super Mario World", List.of("Jump and Run"), "2020-01-01", List.of("NES", "SNES"), "linkToImg");
        Game gameToSave = new Game("1", "Super Mario World", List.of("Jump and Run"), "2020-01-01", List.of("NES", "SNES"), "linkToImg");

        when(gameRepo.save(gameToSave)).thenReturn(gameToSave);
        when(idService.randomId()).thenReturn(gameToSave.id());

        Game actualGame = gameService.addGame(gameDTO);
        Game expectedGame = new Game("1", "Super Mario World", List.of("Jump and Run"), "2020-01-01", List.of("NES", "SNES"), "linkToImg");

        verify(gameRepo).save(gameToSave);
        verify(idService).randomId();
        assertEquals(expectedGame, actualGame);
    }

    @Test
    void updateGame_Success_Test() {
        String id = "1";

        Game existingGame = new Game("1", "Super Mario World", List.of("Jump and Run"), "2020-01-01", List.of("NES", "SNES"), "linkToImg");
        GameDTO updateGameDTO = new GameDTO("Super Mario World 2", List.of("Jump and Run"), "2020-02-01", List.of("NES", "SNES"), "linkToImg");
        Game updatedGame = new Game("1", "Super Mario World 2", List.of("Jump and Run"), "2020-02-01", List.of("NES", "SNES"), "linkToImg");

        when(gameRepo.findById(id)).thenReturn(Optional.of(existingGame));
        when(gameRepo.save(updatedGame)).thenReturn(updatedGame);

        Game expected = new Game("1", "Super Mario World 2", List.of("Jump and Run"), "2020-02-01", List.of("NES", "SNES"), "linkToImg");
        Game actual = gameService.updateGame(updateGameDTO, id);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(gameRepo).findById(id);
        verify(gameRepo).save(updatedGame);
    }

    @Test
    void updateGame_GameNotFound_Test() {
        String id = "1";

        GameDTO updateGameDTO = new GameDTO("Super Mario World 2", List.of("Jump and Run"), "2020-02-01", List.of("NES", "SNES"), "linkToImg");
        Game updatedGame = new Game("1", "Super Mario World 2", List.of("Jump and Run"), "2020-02-01", List.of("NES", "SNES"), "linkToImg");

        when(gameRepo.findById(id)).thenReturn(Optional.empty());

        GameNotFoundException thrown = assertThrows(
                GameNotFoundException.class, () -> gameService.updateGame(updateGameDTO, id)
        );

        assertEquals("No Game found with id: 1", thrown.getMessage());
        verify(gameRepo).findById(id);
        verify(gameRepo, never()).save(updatedGame);
    }

    @Test
    void deleteGame_Test() {
        doNothing().when(gameRepo).deleteById("1");
        gameService.deleteGame("1");
        verify(gameRepo).deleteById("1");
    }

}