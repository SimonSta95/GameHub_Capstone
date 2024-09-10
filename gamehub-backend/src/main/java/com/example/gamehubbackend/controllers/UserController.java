package com.example.gamehubbackend.controllers;

import com.example.gamehubbackend.models.AddGameDTO;
import com.example.gamehubbackend.models.User;
import com.example.gamehubbackend.models.UserDTO;
import com.example.gamehubbackend.models.UserResponse;
import com.example.gamehubbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUser();
    }

    @GetMapping("/me")
    public UserResponse getLoggedInUser() {
        return userService.getLoggedInUser();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @GetMapping("/g/{gitHubId}")
    public UserResponse getUserByGitHubId(@PathVariable String gitHubId) {
        return userService.getUserByGitHubId(gitHubId);
    }

    @PostMapping
    public UserResponse createUser(@RequestBody UserDTO userDTO) {
        return userService.saveUser(userDTO);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id ,@RequestBody UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }

    @PutMapping("/addGame")
    public User addGameToLibrary(@RequestBody AddGameDTO gameToAdd) {
        return userService.addGameToLibrary(gameToAdd.userId(), gameToAdd.game());
    }

    @PutMapping("/deleteGame")
    public User deleteGameFromLibrary(@RequestBody AddGameDTO gameToDelete) {
        return userService.removeGameFromLibrary(gameToDelete.userId(), gameToDelete.game());
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }

}
