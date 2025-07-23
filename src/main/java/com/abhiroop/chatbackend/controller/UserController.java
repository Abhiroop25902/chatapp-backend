package com.abhiroop.chatbackend.controller;

import com.abhiroop.chatbackend.dto.CreateUserRequestDTO;
import com.abhiroop.chatbackend.dto.UserResponseDTO;
import com.abhiroop.chatbackend.entity.User;
import com.abhiroop.chatbackend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail())).toList();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID id) {
        final var userEntityOptional = userRepository.findById(id);

        if (userEntityOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        final var userEntity = userEntityOptional.get();

        return ResponseEntity.status(HttpStatus.FOUND).body(new UserResponseDTO(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getEmail()
        ));
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid CreateUserRequestDTO userData) {
        //check if email already present
        if (userRepository.existsByEmail(userData.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        final var user = new User();
        user.setEmail(userData.email());
        user.setPassword(userData.password());
        final var savedUser = userRepository.save(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new UserResponseDTO(
                        savedUser.getId(),
                        savedUser.getEmail(),
                        savedUser.getPassword()
                ));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> deleteUser(@PathVariable UUID id) {

        final var user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
