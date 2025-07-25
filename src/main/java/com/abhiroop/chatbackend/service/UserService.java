package com.abhiroop.chatbackend.service;

import com.abhiroop.chatbackend.dto.RegisterUserRequestDto;
import com.abhiroop.chatbackend.entity.User;
import com.abhiroop.chatbackend.exception.EmailAlreadyPresentException;
import com.abhiroop.chatbackend.exception.PasswordValidationFailException;
import com.abhiroop.chatbackend.exception.UserNameGenerationFailureException;
import com.abhiroop.chatbackend.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final HashingService hashingService;

    @Autowired
    public UserService(UserRepository userRepository, HashingService hashingService) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
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

        final var user = new User();
        user.setEmail(reqDto.email().trim());

        String hashedPassword = hashingService.hashPassword(reqDto.password());
        user.setPassword(hashedPassword);

        user.setUsername(userName);

        return userRepository.save(user);
    }
}
