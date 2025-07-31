package com.abhiroop.chatbackend.service;

import com.abhiroop.chatbackend.dto.LoginUserRequestDto;
import com.abhiroop.chatbackend.dto.LoginUserResponseDto;
import com.abhiroop.chatbackend.dto.RegisterUserRequestDto;
import com.abhiroop.chatbackend.entity.User;
import com.abhiroop.chatbackend.exception.EmailAlreadyPresentException;
import com.abhiroop.chatbackend.exception.PasswordValidationFailException;
import com.abhiroop.chatbackend.exception.UnauthorizedUserException;
import com.abhiroop.chatbackend.exception.UserNameGenerationFailureException;
import com.abhiroop.chatbackend.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int LOCK_DELAY_IN_DAY = 1;
    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public UserService(UserRepository userRepository, HashingService hashingService, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    private boolean emailAlreadyPresent(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * @param email email of the User
     * @return Username
     * Example: abc@example.com -> abc
     */
    private @NotNull String generateUserNameUsingEmail(@NotNull String email) {
        final var firstPart = email.split("@")[0];
        var secondPart = "";

        int retryCount = 0;

        while (retryCount < 10 && userRepository.existsByUsername(firstPart + secondPart)) {
            retryCount++;
            secondPart = "#" + Math.round(Math.random() * 100_000);
        }

        if (retryCount == 10) {
            throw new UserNameGenerationFailureException("Unable to generate username");
        }

        return secondPart.isEmpty() ? firstPart : firstPart + secondPart;
    }

    /**
     * Checks: 8 Character, should have at least one uppercase, one lowercase and one digit
     *
     * @param password the password string provided by the user
     * @return true if password is valid
     */
    private boolean isPasswordValid(@NotNull String password) {
        if (password.length() < 8) return false;

        boolean uppercasePresent = false;
        boolean lowercasePresent = false;
        boolean digitPresent = false;


        for (char c : password.toCharArray()) {
            if (!uppercasePresent && Character.isUpperCase(c)) uppercasePresent = true;
            if (!lowercasePresent && Character.isLowerCase(c)) lowercasePresent = true;
            if (!digitPresent && Character.isDigit(c)) digitPresent = true;
        }

        return uppercasePresent && lowercasePresent && digitPresent;
    }

    public User saveUser(@NotNull RegisterUserRequestDto reqDto) {
        if (emailAlreadyPresent(reqDto.email())) {
            throw new EmailAlreadyPresentException("User with email " + reqDto.email() + " already exists");
        }

        if (!isPasswordValid(reqDto.password())) {
            throw new PasswordValidationFailException("Password must be more than 8 characters long and must have one uppercase, one lowercase and one digit");
        }

        final var userName = generateUserNameUsingEmail(reqDto.email());
        String hashedPassword = hashingService.hashPassword(reqDto.password());

        return userRepository.save(User.builder()
                .email(reqDto.email().trim())
                .password(hashedPassword)
                .username(userName)
                .build());
    }

    public LoginUserResponseDto loginUser(@NotNull LoginUserRequestDto reqDto) {
        final var existingUserOptional = userRepository.findByEmail(reqDto.email());

        if (existingUserOptional.isEmpty()) throw new UnauthorizedUserException(reqDto.email(), "Invalid credentials");

        final var existingUser = existingUserOptional.get();

        if (existingUser.isAccountLocked() && existingUser.getLockTime().isAfter(LocalDateTime.now()))
            throw new UnauthorizedUserException(reqDto.email(), "Invalid credentials, account locked");

        if (!hashingService.verifyPassword(reqDto.password(), existingUser.getPassword())) {
            existingUser.incrementFailedAttempts();

            if (existingUser.getFailedAttempts() >= MAX_FAILED_ATTEMPTS) {
                existingUser.setLockTime(LocalDateTime.now().plusDays(LOCK_DELAY_IN_DAY));
                existingUser.setFailedAttempts(MAX_FAILED_ATTEMPTS);
                existingUser.setAccountLocked(true);
                userRepository.save(existingUser);
                throw new UnauthorizedUserException(reqDto.email(), "Invalid credentials, account locked");
            }

            userRepository.save(existingUser);
            throw new UnauthorizedUserException(reqDto.email(), "Invalid credentials, you have " + (MAX_FAILED_ATTEMPTS - existingUser.getFailedAttempts()) + " more chance(es)");
        }

        existingUser.unlockAccountForLogin();
        userRepository.save(existingUser);

        final var jwt = jwtService.generateToken(existingUser.getId());
        refreshTokenService.deleteRefreshTokenForUserIfExist(existingUser);
        final var refreshToken = refreshTokenService.generateRefreshTokenForUser(existingUser).getToken();

        return new LoginUserResponseDto(
                existingUser.getId(),
                existingUser.getEmail(),
                "Bearer " + jwt,
                refreshToken
        );
    }
}
