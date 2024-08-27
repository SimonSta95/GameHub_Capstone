package com.example.gamehubbackend.services;

import com.example.gamehubbackend.exceptions.UserNotFoundException;
import com.example.gamehubbackend.models.User;
import com.example.gamehubbackend.models.UserDTO;
import com.example.gamehubbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final IdService idService;

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("No user found with id: " + id));
    }

    public User getUserByGitHubId(String githubId) {
        return userRepository.findByGitHubId(githubId).orElseThrow(() -> new UserNotFoundException("No user found with gitHubId: " + githubId));
    }

    public User saveUser(UserDTO userDTO) {
        User userToSave = new User(
                idService.randomId(),
                userDTO.username(),
                userDTO.gitHubId(),
                userDTO.role(),
                userDTO.gameLibrary(),
                userDTO.creationDate(),
                userDTO.lastUpdateDate()
        );

        return userRepository.save(userToSave);
    }

    public User updateUser(String id, UserDTO userDTO) {
        User user = getUserById(id)
                .withUsername(userDTO.username())
                .withRole(userDTO.role())
                .withGameLibrary(userDTO.gameLibrary())
                .withLastUpdateDate(LocalDateTime.now());

        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
