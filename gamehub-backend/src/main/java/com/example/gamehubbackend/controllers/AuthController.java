package com.example.gamehubbackend.controllers;

import com.example.gamehubbackend.models.UserDTO;
import com.example.gamehubbackend.models.UserResponse;
import com.example.gamehubbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;  // Service for user operations

    /**
     * Get the currently authenticated user's details.
     * Supports both OAuth2 and Username/Password authentication.
     *
     * @param principal the authenticated principal
     * @return UserResponse containing user details
     */
    @GetMapping("/me")
    public UserResponse getCurrentUser(Principal principal) {

        return userService.getLoggedInUser(principal);
    }

    /**
     * Trigger login process.
     * This endpoint does not handle actual login logic.
     */
    @PostMapping("/login")
    public void login() {
        // Only for triggering login; no specific logic
    }

    /**
     * Register a new user.
     *
     * @param userDTO the user data transfer object containing registration details
     * @return UserResponse with details of the newly registered user
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)  // HTTP status code for successful creation
    public UserResponse registerUser(@Validated @RequestBody UserDTO userDTO) {
        return userService.saveUser(userDTO);
    }
}
