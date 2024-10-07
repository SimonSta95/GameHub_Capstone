package com.example.gamehubbackend.services;

import com.example.gamehubbackend.exceptions.UserNotFoundException;
import com.example.gamehubbackend.dto.LibraryGameDTO;
import com.example.gamehubbackend.models.User;
import com.example.gamehubbackend.dto.UserDTO;
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
        LibraryGameDTO game1 = new LibraryGameDTO("game1", "Game 1", List.of("Platform1"), "coverImage1");
        LibraryGameDTO game2 = new LibraryGameDTO("game2", "Game 2", List.of("Platform2"), "coverImage2");

        List<User> users = List.of(
                new User("1","TestUser1", "Test","1","link", "USER", List.of(game1, game2), localDateTime, localDateTime),
                new User("2","TestUser2", "Test","2","link", "USER", List.of(game1), localDateTime, localDateTime)
        );

        List<UserResponse> expected = List.of(
            new UserResponse("1","1","TestUser1","link", "USER", List.of(game1, game2)),
            new UserResponse("2","2","TestUser2","link", "USER", List.of(game1))
        );

        when(userRepository.findAll()).thenReturn(users);

        List<UserResponse> actualUsers = userService.getAllUser();

        verify(userRepository).findAll();
        assertEquals(expected, actualUsers);
    }

    @Test
    void getAllUsers_NoUsers_Test(){
        List<User> users = List.of();
        List<UserResponse> expectedUsers = List.of();
        when(userRepository.findAll()).thenReturn(users);
        List<UserResponse> actualUsers = userService.getAllUser();

        verify(userRepository).findAll();
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void getUserById_Test(){
        LibraryGameDTO game1 = new LibraryGameDTO("game1", "Game 1", List.of("Platform1"), "coverImage1");
        User user = new User("1","TestUser1", "Test","1","link", "USER", List.of(game1), localDateTime, localDateTime);

        UserResponse expected = new UserResponse(user.id(), user.gitHubId(), user.username(), user.avatarUrl(), user.role(), user.gameLibrary());

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        UserResponse actualUser = userService.getUserById("1");

        verify(userRepository).findById("1");
        assertEquals(expected, actualUser);
    }

    @Test
    void getUserById_NoUsers_Test(){
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById("1"));
        verify(userRepository).findById("1");
    }

    @Test
    void getUserByGitHubId_Test(){
        LibraryGameDTO game1 = new LibraryGameDTO("game1", "Game 1", List.of("Platform1"), "coverImage1");
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

        UserDTO userDTO = new UserDTO("TestUser1","Test","1","", "USER", List.of(), localDateTime, localDateTime);
        User userToSave = new User("1","TestUser1", "encodedPassword", "1","", "USER", List.of(), localDateTime, localDateTime); // Set the encoded password

        when(idService.randomId()).thenReturn("1");
        when(passwordEncoder.encode("Test")).thenReturn("encodedPassword"); // Mock password encoding
        when(userRepository.save(userToSave)).thenReturn(userToSave);

        UserResponse actualUser = userService.saveUser(userDTO);
        UserResponse expected = new UserResponse("1","1","TestUser1","","USER",List.of());

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
        LibraryGameDTO game1 = new LibraryGameDTO("game1", "Game 1", List.of("Platform1"), "coverImage1");

        User existingUser = new User("1", "TestUser1", "Test","1","link", "USER", List.of(game1, new LibraryGameDTO("game2", "Game 2", List.of("Platform2"), "coverImage2")), localDateTime, updateDateTime);
        UserDTO updateUserDTO = new UserDTO("TestUser1", "Test","1","link", "ADMIN", List.of(game1),localDateTime, updateDateTime); // Update user role and game library

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));

        try (MockedStatic<LocalDateTime> mockedLocalDate = mockStatic(LocalDateTime.class)) {
            mockedLocalDate.when(LocalDateTime::now).thenReturn(updateDateTime);

            // Update existing user with the DTO values
            existingUser = existingUser.withUsername(updateUserDTO.username())
                    .withRole(updateUserDTO.role())
                    .withGameLibrary(updateUserDTO.gameLibrary());

            UserResponse actualUser = userService.updateUser(id, updateUserDTO);

            // Expected user should reflect the updated information
            UserResponse expectedUser = new UserResponse(
                    existingUser.id(),
                    existingUser.gitHubId(),
                    existingUser.username(),
                    existingUser.avatarUrl(),
                    existingUser.role(),
                    updateUserDTO.gameLibrary() // Use the game library from the DTO
            );

            verify(userRepository).findById(id);
            verify(userRepository).save(existingUser); // Save the updated existingUser
            assertEquals(expectedUser, actualUser);
        }
    }

    @Test
    void updateUser_Test_Failure(){
        String id = "1";
        LibraryGameDTO game1 = new LibraryGameDTO("game1", "Game 1", List.of("Platform1"), "coverImage1");

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
        LibraryGameDTO game1 = new LibraryGameDTO("game1", "Game 1", List.of("Platform1"), "coverImage1");
        LibraryGameDTO game2 = new LibraryGameDTO("game2", "Game 2", List.of("Platform2"), "coverImage2");

        User existingUser = new User("1", "TestUser1", "Test", "1", "link", "USER", new ArrayList<>(List.of(game1)), localDateTime, localDateTime);

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));

        try (MockedStatic<LocalDateTime> mockedLocalDate = mockStatic(LocalDateTime.class)) {
            mockedLocalDate.when(LocalDateTime::now).thenReturn(localDateTime);

            // Don't create a separate updated user, modify the existingUser
            if (!existingUser.gameLibrary().contains(game2)) {
                existingUser.gameLibrary().add(game2);
            }

            UserResponse actualUser = userService.addGameToLibrary(id, game2);

            // Expected user should reflect the added game in the library
            UserResponse expectedUser = new UserResponse(
                    existingUser.id(),
                    existingUser.gitHubId(),
                    existingUser.username(),
                    existingUser.avatarUrl(),
                    existingUser.role(),
                    existingUser.gameLibrary()
            );

            verify(userRepository).findById(id);
            verify(userRepository).save(existingUser); // Save the modified existingUser
            assertEquals(expectedUser, actualUser);
        }
    }

    @Test
    void deleteGameFromLibrary_Test() {
        String userId = "1";
        LibraryGameDTO game1 = new LibraryGameDTO("game1", "Game 1", List.of("Platform1"), "coverImage1");
        LibraryGameDTO game2 = new LibraryGameDTO("game2", "Game 2", List.of("Platform2"), "coverImage2");

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

        UserResponse expected = new UserResponse(
                updatedUser.id(),
                updatedUser.gitHubId(),
                updatedUser.username(),
                updatedUser.avatarUrl(),
                updatedUser.role(),
                updatedUser.gameLibrary()
        );

        // Mock repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call service method to remove game from library
        UserResponse result = userService.removeGameFromLibrary(userId, game2);

        // Verify that the game was removed and the user was saved
        verify(userRepository).findById(userId);
        verify(userRepository).save(argThat(user ->
                user.gameLibrary().equals(updatedUser.gameLibrary()) &&
                        user.id().equals(updatedUser.id())
        ));

        // Assert that the returned user matches the updated user
        assertEquals(expected, result);
    }
}