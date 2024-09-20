package com.example.gamehubbackend.services;

import com.example.gamehubbackend.exceptions.UserNotFoundException;
import com.example.gamehubbackend.models.GameFromFrontendDTO;
import com.example.gamehubbackend.models.User;
import com.example.gamehubbackend.models.UserDTO;
import com.example.gamehubbackend.models.UserResponse;
import com.example.gamehubbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
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
    public List<UserResponse> getAllUser() {
        List<User> userList = userRepository.findAll();// Fetches all users from the repository
        return userList.stream().map(user -> new UserResponse(
                user.id(),
                user.gitHubId(),
                user.username(),
                user.avatarUrl(),
                user.role(),
                user.gameLibrary()
        )).toList();
    }

    /**
     * Get the currently logged-in user's details.
     *
     * @return UserResponse containing details of the logged-in user.
     */
    public UserResponse getLoggedInUser(Principal principal) {

        if (principal instanceof OAuth2AuthenticationToken token) {
            // Handle OAuth2 authentication
            OAuth2User oAuth2User = token.getPrincipal();
            return getUserByUsername(oAuth2User.getAttributes().get("login").toString());
        }

        if (principal instanceof UsernamePasswordAuthenticationToken) {
            var userPrincipal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // Handle Username/Password authentication
            return getUserByUsername(userPrincipal.getUsername());
        }

        return null;
    }

    /**
     * Retrieve user by ID.
     *
     * @param id The user's ID.
     * @return The user if found, else throws UserNotFoundException.
     */
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("No user found with id: " + id));

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
        String avatar = userDTO.avatarUrl() != null  ? userDTO.avatarUrl() : "";
        User userToSave = new User(
                idService.randomId(),
                userDTO.username(),
                passwordEncoder.encode(userDTO.password()),  // Password is encrypted
                userDTO.gitHubId(),
                avatar,
                "USER",
                new ArrayList<>(),
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
    public UserResponse updateUser(String id, UserDTO userDTO) {
        // Fetch the existing user and update the necessary fields
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No user found with id: " + id));

        user = user.withUsername(userDTO.username())
                    .withRole(userDTO.role())
                    .withGameLibrary(userDTO.gameLibrary());

        // Save and return the updated user
        userRepository.save(user);

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
     * Add a game to the user's library.
     *
     * @param userId The user's ID.
     * @param game   The game to be added.
     * @return The updated user with the new game in their library.
     */
    public UserResponse addGameToLibrary(String userId, GameFromFrontendDTO game) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("No user found with id: " + userId));

        // Add the game if it is not already in the user's library
        if (!user.gameLibrary().contains(game)) {
            user.gameLibrary().add(game);
        }

        userRepository.save(user);

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
     * Remove a game from the user's library.
     *
     * @param userId The user's ID.
     * @param game   The game to be removed.
     * @return The updated user after removing the game.
     */
    public UserResponse removeGameFromLibrary(String userId, GameFromFrontendDTO game) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("No user found with id: " + userId));

        // Remove the game from the user's library
        user.gameLibrary().remove(game);
        userRepository.save(user);

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
     * Delete a user by ID.
     *
     * @param id The user's ID.
     * @return no content and 204 status
     */
    public ResponseEntity<Object> deleteUser(String id) {
        // Remove the user from the database
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
