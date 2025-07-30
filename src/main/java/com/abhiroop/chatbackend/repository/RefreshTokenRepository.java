package com.abhiroop.chatbackend.repository;


import com.abhiroop.chatbackend.entity.RefreshToken;
import com.abhiroop.chatbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByExpireAtBefore(LocalDateTime expireAtBefore);

    Optional<RefreshToken> findByUser(User user);
}
