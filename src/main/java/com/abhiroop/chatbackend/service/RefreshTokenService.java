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
        final var refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(generateRefreshTokenString());
        refreshToken.setExpireAt(LocalDateTime.now().plusDays(7));

        return refreshTokenRepository.save(refreshToken);
    }
}
