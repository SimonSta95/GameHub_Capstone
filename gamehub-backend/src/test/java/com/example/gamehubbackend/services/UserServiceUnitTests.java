package com.example.gamehubbackend.services;

import com.example.gamehubbackend.exceptions.UserNotFoundException;
import com.example.gamehubbackend.models.AddGameDTO;
import com.example.gamehubbackend.models.User;
import com.example.gamehubbackend.models.UserDTO;
import com.example.gamehubbackend.repositories.UserRepository;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceUnitTests {

    private final IdService idService = mock(IdService.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = new UserService(userRepository, idService);
    private final LocalDateTime localDateTime = LocalDateTime.parse("2020-01-01T01:00:00");
    private final LocalDateTime updateDateTime = LocalDateTime.parse("2020-01-01T02:00:00");

    @Test
    void getAllUsers_Test(){
        List<User> users = List.of(
                new User("1","TestUser1", "1","link", "USER", List.of("Game 1", "Game 2"), localDateTime, localDateTime),
                new User("2","TestUser2", "2","link", "USER", List.of("Game 3", "Game 4"), localDateTime, localDateTime)
        );

        List<User> expectedUsers = List.of(
                new User("1","TestUser1", "1","link", "USER", List.of("Game 1", "Game 2"), localDateTime, localDateTime),
                new User("2","TestUser2", "2","link", "USER", List.of("Game 3", "Game 4"), localDateTime, localDateTime)
        );

        when(userRepository.findAll()).thenReturn(users);
        List<User> actualUsers = userService.getAllUser();

        verify(userRepository).findAll();
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void getAllUsers_NoUsers_Test(){
        List<User> expectedUsers = List.of();
        List<User> actualUsers = userService.getAllUser();

        verify(userRepository).findAll();
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void getUserById_Test(){
        User user = new User("1","TestUser1", "1","link", "USER", List.of("Game 1", "Game 2"), localDateTime, localDateTime);
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        User actualUser = userService.getUserById("1");
        User expectedUser = new User("1","TestUser1", "1","link", "USER", List.of("Game 1", "Game 2"), localDateTime, localDateTime);

        verify(userRepository).findById("1");
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getUserById_NoUsers_Test(){
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById("1"));
        verify(userRepository).findById("1");
    }

    @Test
    void getUserByGitHubId_Test(){
        User user = new User("1","TestUser1", "1","link", "USER", List.of("Game 1", "Game 2"), localDateTime, localDateTime);
        when(userRepository.findByGitHubId("1")).thenReturn(Optional.of(user));

        User actualUser = userService.getUserByGitHubId("1");
        User expectedUser = new User("1","TestUser1", "1","link", "USER", List.of("Game 1", "Game 2"), localDateTime, localDateTime);

        verify(userRepository).findByGitHubId("1");
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getUserByGitHubId_NoUsers_Test(){
        when(userRepository.findByGitHubId("1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByGitHubId("1"));
        verify(userRepository).findByGitHubId("1");
    }

    @Test
    void createUser_Test(){
        UserDTO userDTO = new UserDTO("TestUser1","1","link", "USER", List.of("Game 1", "Game 2"), localDateTime, localDateTime);
        User userToSave = new User("1","TestUser1", "1","link", "USER", List.of("Game 1", "Game 2"), localDateTime, localDateTime);

        when(idService.randomId()).thenReturn("1");
        when(userRepository.save(userToSave)).thenReturn(userToSave);

        User actualUser = userService.saveUser(userDTO);

        User expectedUser = new User("1","TestUser1", "1","link", "USER", List.of("Game 1", "Game 2"), localDateTime, localDateTime);
        verify(idService).randomId();
        verify(userRepository).save(userToSave);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void deleteUser_Test(){
        doNothing().when(userRepository).deleteById("1");
        userService.deleteUser("1");
        verify(userRepository).deleteById("1");
    }

    @Test
    void updateUser_Test_Success(){
        String id = "1";

        User existingUser = new User("1", "TestUser1", "1","link", "USER", List.of("Game 1", "Game 2"), localDateTime, localDateTime);
        UserDTO updateUserDTO = new UserDTO("TestUser1", "1","link", "USER", List.of("Game 1", "Game 2", "Game 3"), localDateTime, updateDateTime);
        User updatedUser = new User("1", "TestUser1", "1","link", "USER", List.of("Game 1", "Game 2", "Game 3"), localDateTime, updateDateTime);

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        try (MockedStatic<LocalDateTime> mockedLocalDate = mockStatic(LocalDateTime.class)) {
            mockedLocalDate.when(LocalDateTime::now).thenReturn(updateDateTime);

            User actualUser = userService.updateUser(id, updateUserDTO);

            User expectedUser = new User("1", "TestUser1", "1","link", "USER", List.of("Game 1", "Game 2", "Game 3"), localDateTime, updateDateTime);

            mockedLocalDate.verify(LocalDateTime::now);
            verify(userRepository).findById(id);
            verify(userRepository).save(updatedUser);
            assertEquals(expectedUser, actualUser);
        }
    }

    @Test
    void updateUser_Test_Failure(){
        String id = "1";
        UserDTO userDTO = new UserDTO("TestUser1", "1","link", "USER", List.of("Game 1", "Game 2", "Game 3"), localDateTime, updateDateTime);
        User updatedUser = new User("1", "TestUser1", "1","link", "USER", List.of("Game 1", "Game 2", "Game 3"), localDateTime, updateDateTime);

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(id, userDTO)
        );

        verify(userRepository).findById(id);
        verify(userRepository, never()).save(updatedUser);
        assertEquals("No user found with id: 1",thrown.getMessage());

    }

    @Test
    void addGameToLibrary_Test(){
        String id = "1";

        User existingUser = new User("1", "TestUser1", "1", "link", "USER",
                new ArrayList<>(List.of("Game 1", "Game 2")), localDateTime, localDateTime);

        AddGameDTO gameToAddDTO = new AddGameDTO("1", "Game 3");

        User updatedUser = new User("1", "TestUser1", "1", "link", "USER",
                new ArrayList<>(List.of("Game 1", "Game 2", "Game 3")), localDateTime, localDateTime);

        when(userRepository.findByGitHubId(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);

        User actualUser = userService.addGameToLibrary(gameToAddDTO.userId(), gameToAddDTO.gameId());

        User expectedUser = new User("1", "TestUser1", "1", "link", "USER",
                List.of("Game 1", "Game 2", "Game 3"), localDateTime, localDateTime);

        verify(userRepository).findByGitHubId(id);
        verify(userRepository).save(existingUser);
        assertEquals(expectedUser, actualUser);

    }

    @Test
    void deleteGameFromLibrary_Test(){
        String id = "1";

        User existingUser = new User("1", "TestUser1", "1", "link", "USER",
                new ArrayList<>(List.of("Game 1", "Game 2")), localDateTime, localDateTime);

        AddGameDTO gameToDeleteDTO = new AddGameDTO("1", "Game 2");

        User updatedUser = new User("1", "TestUser1", "1", "link", "USER",
                new ArrayList<>(List.of("Game 1")), localDateTime, localDateTime);

        when(userRepository.findByGitHubId(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);

        User actualUser = userService.addGameToLibrary(gameToDeleteDTO.userId(), gameToDeleteDTO.gameId());

        User expectedUser = new User("1", "TestUser1", "1", "link", "USER",
                List.of("Game 1"), localDateTime, localDateTime);

        verify(userRepository).findByGitHubId(id);
        verify(userRepository).save(existingUser);
        assertEquals(expectedUser, actualUser);
    }
}