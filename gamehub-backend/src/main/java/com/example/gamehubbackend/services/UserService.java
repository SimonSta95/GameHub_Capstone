package com.example.gamehubbackend.services;

import com.example.gamehubbackend.exceptions.UserNotFoundException;
import com.example.gamehubbackend.models.GameFromFrontendDTO;
import com.example.gamehubbackend.models.User;
import com.example.gamehubbackend.models.UserDTO;
import com.example.gamehubbackend.models.UserResponse;
import com.example.gamehubbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    // Dependencies injected through the constructor
    private final UserRepository userRepository;
    private final IdService idService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Retrieve all users from the database.
     *
     * @return List of all users.
     */
    public List<User> getAllUser() {
        return userRepository.findAll();  // Fetches all users from the repository
    }

    /**
     * Get the currently logged-in user's details.
     *
     * @return UserResponse containing details of the logged-in user.
     */
    public UserResponse getLoggedInUser() {
        var principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getUserByUsername(principal.getUsername());  // Fetch user details by username
    }

    /**
     * Retrieve user by ID.
     *
     * @param id The user's ID.
     * @return The user if found, else throws UserNotFoundException.
     */
    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("No user found with id: " + id));
    }

    /**
     * Retrieve user by GitHub ID.
     *
     * @param githubId The user's GitHub ID.
     * @return UserResponse containing the user's details.
     */
    public UserResponse getUserByGitHubId(String githubId) {
        User user = userRepository.findByGitHubId(githubId).orElseThrow(() -> new UserNotFoundException("No user found with gitHubId: " + githubId));

        // Return user details in a UserResponse object
        return new UserResponse(
                user.id(),
                user.gitHubId(),
                user.username(),
                user.avatarUrl(),
                user.role(),
                user.gameLibrary()
        );
    }

    /**
     * Retrieve user by username.
     *
     * @param username The user's username.
     * @return UserResponse containing the user's details.
     */
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("No user found with username: " + username));

        // Return user details in a UserResponse object
        return new UserResponse(
                user.id(),
                user.gitHubId(),
                user.username(),
                user.avatarUrl(),
                user.role(),
                user.gameLibrary()
        );
    }

    /**
     * Register a new user in the system.
     *
     * @param userDTO The user data transfer object containing the new user details.
     * @return UserResponse with the newly registered user's details.
     */
    public UserResponse saveUser(UserDTO userDTO) {
        // Create a new user with a random ID and encrypted password
        User userToSave = new User(
                idService.randomId(),
                userDTO.username(),
                passwordEncoder.encode(userDTO.password()),  // Password is encrypted
                userDTO.gitHubId(),
                userDTO.avatarUrl(),
                userDTO.role(),
                userDTO.gameLibrary(),
                userDTO.creationDate(),
                userDTO.lastUpdateDate()
        );

        // Save the new user in the database
        userRepository.save(userToSave);

        // Return the saved user's details
        return new UserResponse(
                userToSave.id(),
                userToSave.gitHubId(),
                userToSave.username(),
                userToSave.avatarUrl(),
                userToSave.role(),
                userToSave.gameLibrary()
        );
    }

    /**
     * Update an existing user's details.
     *
     * @param id      The user's ID.
     * @param userDTO The new user details.
     * @return The updated user.
     */
    public User updateUser(String id, UserDTO userDTO) {
        // Fetch the existing user and update the necessary fields
        User user = getUserById(id)
                .withUsername(userDTO.username())
                .withRole(userDTO.role())
                .withGameLibrary(userDTO.gameLibrary())
                .withLastUpdateDate(LocalDateTime.now());  // Update the timestamp

        // Save and return the updated user
        return userRepository.save(user);
    }

    /**
     * Add a game to the user's library.
     *
     * @param userId The user's ID.
     * @param game   The game to be added.
     * @return The updated user with the new game in their library.
     */
    public User addGameToLibrary(String userId, GameFromFrontendDTO game) {
        User user = getUserById(userId);

        // Add the game if it is not already in the user's library
        if (!user.gameLibrary().contains(game)) {
            user.gameLibrary().add(game);
        }

        return userRepository.save(user);
    }

    /**
     * Remove a game from the user's library.
     *
     * @param userId The user's ID.
     * @param game   The game to be removed.
     * @return The updated user after removing the game.
     */
    public User removeGameFromLibrary(String userId, GameFromFrontendDTO game) {
        User user = getUserById(userId);

        // Remove the game from the user's library
        user.gameLibrary().remove(game);

        return userRepository.save(user);
    }

    /**
     * Delete a user by ID.
     *
     * @param id The user's ID.
     */
    public void deleteUser(String id) {
        // Remove the user from the database
        userRepository.deleteById(id);
    }
}
