package com.example.gamehubbackend.controllers;

import com.example.gamehubbackend.models.UserDTO;
import com.example.gamehubbackend.models.UserResponse;
import com.example.gamehubbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/me")
    public UserResponse getCurrentUser(@AuthenticationPrincipal OAuth2User user) {
        return userService.getUserByUsername(user.getAttributes().get("login").toString());
    }

    @PostMapping("/login")
    public void login() {
        //only to trigger login
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(@Validated @RequestBody UserDTO userDTO) {
        return userService.saveUser(userDTO);
    }
}
