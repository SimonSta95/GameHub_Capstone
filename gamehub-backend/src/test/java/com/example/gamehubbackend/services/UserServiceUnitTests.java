package com.example.gamehubbackend.services;

import com.example.gamehubbackend.exceptions.UserNotFoundException;
import com.example.gamehubbackend.models.FrontendGame;
import com.example.gamehubbackend.models.User;
import com.example.gamehubbackend.models.UserDTO;
import com.example.gamehubbackend.models.UserResponse;
import com.example.gamehubbackend.repositories.UserRepository;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceUnitTests {

    private final IdService idService = mock(IdService.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final UserService userService = new UserService(userRepository, idService, passwordEncoder);
    private final LocalDateTime localDateTime = LocalDateTime.parse("2020-01-01T01:00:00");
    private final LocalDateTime updateDateTime = LocalDateTime.parse("2020-01-01T02:00:00");

    @Test
    void getAllUsers_Test(){
        FrontendGame game1 = new FrontendGame("game1", "Game 1", List.of("Platform1"), "coverImage1");
        FrontendGame game2 = new FrontendGame("game2", "Game 2", List.of("Platform2"), "coverImage2");

        List<User> users = List.of(
                new User("1","TestUser1", "Test","1","link", "USER", List.of(game1, game2), localDateTime, localDateTime),
                new User("2","TestUser2", "Test","2","link", "USER", List.of(game1), localDateTime, localDateTime)
        );

        when(userRepository.findAll()).thenReturn(users);

        List<User> actualUsers = userService.getAllUser();

        verify(userRepository).findAll();
        assertEquals(users, actualUsers);
    }

    @Test
    void getAllUsers_NoUsers_Test(){
        List<User> expectedUsers = List.of();
        when(userRepository.findAll()).thenReturn(expectedUsers);
        List<User> actualUsers = userService.getAllUser();

        verify(userRepository).findAll();
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void getUserById_Test(){
        FrontendGame game1 = new FrontendGame("game1", "Game 1", List.of("Platform1"), "coverImage1");
        User user = new User("1","TestUser1", "Test","1","link", "USER", List.of(game1), localDateTime, localDateTime);

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        User actualUser = userService.getUserById("1");

        verify(userRepository).findById("1");
        assertEquals(user, actualUser);
    }

    @Test
    void getUserById_NoUsers_Test(){
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById("1"));
        verify(userRepository).findById("1");
    }

    @Test
    void getUserByGitHubId_Test(){
        FrontendGame game1 = new FrontendGame("game1", "Game 1", List.of("Platform1"), "coverImage1");
        User user = new User("1","TestUser1", "Test","1","link", "USER", List.of(game1), localDateTime, localDateTime);

        UserResponse expected = new UserResponse("1","1","TestUser1","link","USER",List.of(game1));

        when(userRepository.findByGitHubId("1")).thenReturn(Optional.of(user));

        UserResponse actualUser = userService.getUserByGitHubId("1");

        verify(userRepository).findByGitHubId("1");
        assertEquals(expected, actualUser);
    }

    @Test
    void getUserByGitHubId_NoUsers_Test(){
        when(userRepository.findByGitHubId("1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByGitHubId("1"));
        verify(userRepository).findByGitHubId("1");
    }

    @Test
    void createUser_Test(){
        FrontendGame game1 = new FrontendGame("game1", "Game 1", List.of("Platform1"), "coverImage1");
        FrontendGame game2 = new FrontendGame("game2", "Game 2", List.of("Platform2"), "coverImage2");

        UserDTO userDTO = new UserDTO("TestUser1","Test","1","link", "USER", List.of(game1, game2), localDateTime, localDateTime);
        User userToSave = new User("1","TestUser1", "encodedPassword", "1","link", "USER", List.of(game1, game2), localDateTime, localDateTime); // Set the encoded password

        when(idService.randomId()).thenReturn("1");
        when(passwordEncoder.encode("Test")).thenReturn("encodedPassword"); // Mock password encoding
        when(userRepository.save(userToSave)).thenReturn(userToSave);

        UserResponse actualUser = userService.saveUser(userDTO);
        UserResponse expected = new UserResponse("1","1","TestUser1","link","USER",List.of(game1, game2));

        verify(idService).randomId();
        verify(passwordEncoder).encode("Test"); // Verify that encoding was called
        verify(userRepository).save(userToSave);
        assertEquals(expected, actualUser);
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
        FrontendGame game1 = new FrontendGame("game1", "Game 1", List.of("Platform1"), "coverImage1");
        FrontendGame game2 = new FrontendGame("game2", "Game 2", List.of("Platform2"), "coverImage2");

        User existingUser = new User("1", "TestUser1", "Test","1","link", "USER", List.of(game1, game2), localDateTime, localDateTime);
        UserDTO updateUserDTO = new UserDTO("TestUser1", "Test","1","link", "USER", List.of(game1), localDateTime, updateDateTime);
        User updatedUser = new User("1", "TestUser1", "Test","1","link", "USER", List.of(game1), localDateTime, updateDateTime);

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        try (MockedStatic<LocalDateTime> mockedLocalDate = mockStatic(LocalDateTime.class)) {
            mockedLocalDate.when(LocalDateTime::now).thenReturn(updateDateTime);

            User actualUser = userService.updateUser(id, updateUserDTO);

            verify(userRepository).findById(id);
            verify(userRepository).save(updatedUser);
            assertEquals(updatedUser, actualUser);
        }
    }

    @Test
    void updateUser_Test_Failure(){
        String id = "1";
        FrontendGame game1 = new FrontendGame("game1", "Game 1", List.of("Platform1"), "coverImage1");

        UserDTO userDTO = new UserDTO("TestUser1", "Test","1","link", "USER", List.of(game1), localDateTime, updateDateTime);

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(id, userDTO)
        );

        verify(userRepository).findById(id);
        verify(userRepository, never()).save(any(User.class));
        assertEquals("No user found with id: 1", thrown.getMessage());
    }

    @Test
    void addGameToLibrary_Test() {
        String id = "1";
        FrontendGame game1 = new FrontendGame("game1", "Game 1", List.of("Platform1"), "coverImage1");
        FrontendGame game2 = new FrontendGame("game2", "Game 2", List.of("Platform2"), "coverImage2");

        User existingUser = new User("1", "TestUser1", "Test", "1", "link", "USER", new ArrayList<>(List.of(game1)), localDateTime, localDateTime);
        User updatedUser = new User("1", "TestUser1", "Test", "1", "link", "USER", new ArrayList<>(List.of(game1, game2)), localDateTime, localDateTime);

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User actualUser = userService.addGameToLibrary(id, game2);

        verify(userRepository).findById(id);
        verify(userRepository).save(updatedUser);
        assertEquals(updatedUser, actualUser);
    }

    @Test
    void deleteGameFromLibrary_Test() {
        String userId = "1";
        FrontendGame game1 = new FrontendGame("game1", "Game 1", List.of("Platform1"), "coverImage1");
        FrontendGame game2 = new FrontendGame("game2", "Game 2", List.of("Platform2"), "coverImage2");

        // Mocking an existing user with both games in their library
        User existingUser = new User(
                userId,
                "TestUser1",
                "TestPassword",
                "1",
                "avatarUrl",
                "USER",
                new ArrayList<>(List.of(game1, game2)),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Create expected user after removing game2
        User updatedUser = existingUser.withGameLibrary(new ArrayList<>(List.of(game1))); // Only game1 remains

        // Mock repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call service method to remove game from library
        User result = userService.removeGameFromLibrary(userId, game2);

        // Verify that the game was removed and the user was saved
        verify(userRepository).findById(userId);
        verify(userRepository).save(argThat(user ->
                user.gameLibrary().equals(updatedUser.gameLibrary()) &&
                        user.id().equals(updatedUser.id())
        ));

        // Assert that the returned user matches the updated user
        assertEquals(updatedUser, result);
    }
}