package com.abhiroop.chatbackend.controller;

import com.abhiroop.chatbackend.dto.RegisterUserRequestDto;
import com.abhiroop.chatbackend.dto.RegisterUserResponseDto;
import com.abhiroop.chatbackend.service.UserService;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
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


    @RateLimiter(name = "registerRateLimiter", fallbackMethod = "rateLimitFallback")
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

    public ResponseEntity<RegisterUserResponseDto> rateLimitFallback(RegisterUserRequestDto requestDto, RequestNotPermitted ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

}
