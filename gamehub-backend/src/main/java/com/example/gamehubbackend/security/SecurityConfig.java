package com.example.gamehubbackend.security;

import com.example.gamehubbackend.exceptions.UserNotFoundException;
import com.example.gamehubbackend.models.User;
import com.example.gamehubbackend.models.UserDTO;
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

    private final UserService userService;

    @Value("${APP_URL}")
    private String appUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(a -> a
//                        .requestMatchers("/api/games").authenticated()
//                        .requestMatchers("/api/games/**").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .oauth2Login(o -> o.defaultSuccessUrl(appUrl))
                .logout(l -> l.logoutSuccessUrl(appUrl));
        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        return request -> {
            OAuth2User user = delegate.loadUser(request);
            User gitHubUser;
            try {
                gitHubUser = userService.getUserByGitHubId(user.getName());
            } catch (UserNotFoundException e) {
                gitHubUser = userService.saveUser(new UserDTO(
                        user.getAttributes().get("login").toString(),
                        user.getName(),
                        "USER",
                        new ArrayList<>(),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                        ));
            }

            return new DefaultOAuth2User(List.of(
                    new SimpleGrantedAuthority(gitHubUser.role())),
                    user.getAttributes(),
                    "id"
            );
        };
    }
}
