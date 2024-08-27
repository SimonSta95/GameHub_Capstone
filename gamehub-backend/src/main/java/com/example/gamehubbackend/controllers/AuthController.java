package com.example.gamehubbackend.controllers;

import com.example.gamehubbackend.models.User;
import com.example.gamehubbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/me")
    public User getCurrentUser(@AuthenticationPrincipal OAuth2User user) {
        return userService.getUserByGitHubId(user.getName());
    }
}
