package com.example.gamehubbackend.config;

import com.example.gamehubbackend.exceptions.UserNotFoundException;
import com.example.gamehubbackend.models.UserDTO;
import com.example.gamehubbackend.models.UserResponse;
import com.example.gamehubbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;  // Service for user-related operations

    @Value("${APP_URL}")
    private String appUrl;  // Application URL for OAuth2 login redirection

    /**
     * Configure security settings for HTTP requests.
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)  // Disable CORS configuration for simplicity
                .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF protection for API endpoints
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/api/games/**").authenticated()  // Require authentication for game-related endpoints
                        .requestMatchers("/api/auth/me").authenticated()  // Require authentication for user info endpoint
                        .anyRequest().permitAll()  // Allow all other requests without authentication
                )
                .httpBasic(httpSecurityHttpBasicConfigurer ->
                        httpSecurityHttpBasicConfigurer.authenticationEntryPoint((request, response, authException) -> response.sendError(401)))  // Custom entry point for HTTP Basic auth
                .oauth2Login(o -> o.defaultSuccessUrl(appUrl))  // Configure OAuth2 login with a default redirect URL
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))  // Ensure sessions are always created
                .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))  // Handle unauthorized access
                .logout(l -> l.logoutSuccessUrl(appUrl));  // Redirect to app URL after logout

        return http.build();  // Build and return the configured SecurityFilterChain
    }

    /**
     * Define an OAuth2UserService bean to handle OAuth2 user details.
     *
     * @return an OAuth2UserService instance
     */
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();  // Default service for loading user details

        return request -> {
            OAuth2User user = delegate.loadUser(request);  // Load user details from the OAuth2 provider
            UserResponse gitHubUser;
            try {
                gitHubUser = userService.getUserByGitHubId(user.getName());  // Fetch existing user from database
            } catch (UserNotFoundException e) {
                // If user not found, create and save a new user
                gitHubUser = userService.saveUser(new UserDTO(
                        user.getAttributes().get("login").toString(),
                        "",
                        user.getName(),
                        user.getAttributes().get("avatar_url").toString(),
                        "USER",  // Default user role
                        new ArrayList<>(),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                ));
            }

            // Create and return a DefaultOAuth2User with user attributes and authorities
            return new DefaultOAuth2User(List.of(
                    new SimpleGrantedAuthority(gitHubUser.role())),  // Set user role authority
                    user.getAttributes(),
                    "id"  // Key attribute for user principal
            );
        };
    }
}
