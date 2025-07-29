package com.abhiroop.chatbackend.service;

import com.abhiroop.chatbackend.config.JwtConfig;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class JwtService {

    private final JwtConfig jwtConfig;
    private final JwtBuilder jwtBuilder;
    private final JwtParser jwtParser;

    @Autowired
    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getSecret()));
        this.jwtBuilder = Jwts.builder().signWith(key);
        this.jwtParser = Jwts.parser().verifyWith(key).build();
    }

    public String generateToken(UUID uuid) {
        return jwtBuilder
                .subject(uuid.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .compact();
    }

    private UUID getUuidFromToken(String token) {
        return UUID.fromString(jwtParser.parseSignedClaims(token).getPayload().getSubject());
    }
}
