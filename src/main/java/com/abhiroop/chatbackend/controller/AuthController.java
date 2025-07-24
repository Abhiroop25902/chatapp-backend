package com.abhiroop.chatbackend.controller;

import com.abhiroop.chatbackend.dto.RegisterUserRequestDto;
import com.abhiroop.chatbackend.dto.RegisterUserResponseDto;
import com.abhiroop.chatbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/api/vi/auth/register")
    public ResponseEntity<RegisterUserResponseDto> registerUser(@RequestBody RegisterUserRequestDto requestDto) {

        final var createdUser = userService.saveUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new RegisterUserResponseDto(
                        createdUser.getId(),
                        createdUser.getUsername(),
                        createdUser.getEmail()
                )
        );
    }

}
