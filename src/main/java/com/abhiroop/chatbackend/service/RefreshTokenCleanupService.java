package com.abhiroop.chatbackend.service;

import com.abhiroop.chatbackend.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RefreshTokenCleanupService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenCleanupService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // Every Day Once
    @Scheduled(fixedRate = 86400000)
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteByExpireAtBefore(LocalDateTime.now());
    }
}
