package com.abhiroop.chatbackend.service;

import com.abhiroop.chatbackend.entity.RefreshToken;
import com.abhiroop.chatbackend.entity.User;
import com.abhiroop.chatbackend.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class RefreshTokenService {
    private static final int REFRESH_TOKEN_EXPIRY_DELAY_IN_DAY = 7;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    private static String generateRefreshTokenString() {
        byte[] tokenBytes = new byte[64];
        new SecureRandom().nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    public RefreshToken generateRefreshTokenForUser(User user) {
        return refreshTokenRepository.save(RefreshToken.builder()
                .user(user)
                .token(generateRefreshTokenString())
                .expireAt(LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRY_DELAY_IN_DAY))
                .build()
        );
    }

    public void deleteRefreshTokenForUserIfExist(User user) {
        final var refreshToken = refreshTokenRepository.findByUser(user);

        refreshToken.ifPresent(refreshTokenRepository::delete);
    }
}
