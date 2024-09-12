package com.example.gamehubbackend.controllers;

import com.example.gamehubbackend.models.UserDTO;
import com.example.gamehubbackend.models.UserResponse;
import com.example.gamehubbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/me")
    public UserResponse getCurrentUser(Principal principal) {

        if (principal instanceof OAuth2AuthenticationToken token) {
            OAuth2User oAuth2User = token.getPrincipal();
            return userService.getUserByUsername(oAuth2User.getAttributes().get("login").toString());
        }

        if (principal instanceof UsernamePasswordAuthenticationToken) {
            return userService.getLoggedInUser();
        }

        return null;
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
