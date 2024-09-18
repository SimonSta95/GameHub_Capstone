package com.example.gamehubbackend.controllers;

import com.example.gamehubbackend.models.AddGameDTO;
import com.example.gamehubbackend.models.UserDTO;
import com.example.gamehubbackend.models.UserResponse;
import com.example.gamehubbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;  // Service for handling user operations

    /**
     * Retrieve a list of all users.
     *
     * @return a list of User objects
     */
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUser();
    }

    /**
     * Retrieve a specific user by their ID.
     *
     * @param id the ID of the user
     * @return the User object with the specified ID
     */
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    /**
     * Retrieve a user by their GitHub ID.
     *
     * @param gitHubId the GitHub ID of the user
     * @return the UserResponse object containing user details
     */
    @GetMapping("/g/{gitHubId}")
    public UserResponse getUserByGitHubId(@PathVariable String gitHubId) {
        return userService.getUserByGitHubId(gitHubId);
    }

    /**
     * Create a new user.
     *
     * @param userDTO the UserDTO object containing user details
     * @return the newly created UserResponse object
     */
    @PostMapping
    public UserResponse createUser(@RequestBody UserDTO userDTO) {
        return userService.saveUser(userDTO);
    }

    /**
     * Update an existing user's information.
     *
     * @param id the ID of the user to update
     * @param userDTO the UserDTO object containing updated user details
     * @return the updated User object
     */
    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }

    /**
     * Add a game to a user's library.
     *
     * @param gameToAdd the AddGameDTO object containing user ID and game details
     * @return the updated User object with the game added
     */
    @PutMapping("/addGame")
    public UserResponse addGameToLibrary(@RequestBody AddGameDTO gameToAdd) {
        return userService.addGameToLibrary(gameToAdd.userId(), gameToAdd.game());
    }

    /**
     * Remove a game from a user's library.
     *
     * @param gameToDelete the AddGameDTO object containing user ID and game details
     * @return the updated User object with the game removed
     */
    @PutMapping("/deleteGame")
    public UserResponse deleteGameFromLibrary(@RequestBody AddGameDTO gameToDelete) {
        return userService.removeGameFromLibrary(gameToDelete.userId(), gameToDelete.game());
    }

    /**
     * Delete a user by their ID.
     *
     * @param id the ID of the user to delete
     * @return no content and status 204
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
