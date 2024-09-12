package com.example.gamehubbackend.services;

import com.example.gamehubbackend.exceptions.UserNotFoundException;
import com.example.gamehubbackend.models.FrontendGame;
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

    private final UserRepository userRepository;
    private final IdService idService;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public UserResponse getLoggedInUser() {
        var principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getUserByUsername(principal.getUsername());
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("No user found with id: " + id));
    }

    public UserResponse getUserByGitHubId(String githubId) {
        User user = userRepository.findByGitHubId(githubId).orElseThrow(() -> new UserNotFoundException("No user found with gitHubId: " + githubId));

        return new UserResponse(
                user.id(),
                user.gitHubId(),
                user.username(),
                user.avatarUrl(),
                user.role(),
                user.gameLibrary()
        );
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("No user found with username: " + username));

        return new UserResponse(
                user.id(),
                user.gitHubId(),
                user.username(),
                user.avatarUrl(),
                user.role(),
                user.gameLibrary()
        );
    }

    public UserResponse saveUser(UserDTO userDTO) {


        User userToSave = new User(
                idService.randomId(),
                userDTO.username(),
                passwordEncoder.encode(userDTO.password()),
                userDTO.gitHubId(),
                userDTO.avatarUrl(),
                userDTO.role(),
                userDTO.gameLibrary(),
                userDTO.creationDate(),
                userDTO.lastUpdateDate()
        );

        userRepository.save(userToSave);

        return new UserResponse(
                userToSave.id(),
                userToSave.gitHubId(),
                userToSave.username(),
                userToSave.avatarUrl(),
                userToSave.role(),
                userToSave.gameLibrary()
        );
    }

    public User updateUser(String id, UserDTO userDTO) {

        User user = getUserById(id)
                .withUsername(userDTO.username())
                .withRole(userDTO.role())
                .withGameLibrary(userDTO.gameLibrary())
                .withLastUpdateDate(LocalDateTime.now());

        return userRepository.save(user);
    }

    public User addGameToLibrary(String userId, FrontendGame game) {
        User user = getUserById(userId);

        if(!user.gameLibrary().contains(game)) {

            user.gameLibrary().add(game);
        }

        return userRepository.save(user);
    }

    public User removeGameFromLibrary(String userId,FrontendGame game) {
        User user = getUserById(userId);

        user.gameLibrary().remove(game);

        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
