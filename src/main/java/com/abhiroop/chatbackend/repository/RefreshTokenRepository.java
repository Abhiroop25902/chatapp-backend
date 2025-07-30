package com.abhiroop.chatbackend.repository;


import com.abhiroop.chatbackend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByExpireAtBefore(LocalDateTime expireAtBefore);
}
