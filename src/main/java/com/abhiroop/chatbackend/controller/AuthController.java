package com.abhiroop.chatbackend.controller;

import com.abhiroop.chatbackend.dto.LoginUserRequestDto;
import com.abhiroop.chatbackend.dto.LoginUserResponseDto;
import com.abhiroop.chatbackend.dto.RegisterUserRequestDto;
import com.abhiroop.chatbackend.dto.RegisterUserResponseDto;
import com.abhiroop.chatbackend.service.UserService;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @RateLimiter(name = "registerRateLimiter", fallbackMethod = "rateLimitFallback")
    @PostMapping("/api/v1/auth/register")
    public ResponseEntity<RegisterUserResponseDto> registerUser(@Valid @RequestBody RegisterUserRequestDto requestDto) {
        final var createdUser = userService.saveUser(requestDto);
        log.info("created user {}", createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new RegisterUserResponseDto(
                        createdUser.getId(),
                        createdUser.getUsername(),
                        createdUser.getEmail()
                )
        );
    }

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<LoginUserResponseDto> loginUser(@Valid @RequestBody LoginUserRequestDto requestDto) {
        final LoginUserResponseDto responseDto = userService.loginUser(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    public ResponseEntity<RegisterUserResponseDto> rateLimitFallback(RegisterUserRequestDto requestDto, RequestNotPermitted ex) {
        log.warn(ex.toString());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

}
