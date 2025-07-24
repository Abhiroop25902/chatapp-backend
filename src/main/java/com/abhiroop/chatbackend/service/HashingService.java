package com.abhiroop.chatbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class HashingService {
    final PasswordEncoder encoder;

    @Autowired
    public HashingService(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public String hashPassword(String password) {
        return encoder.encode(password);
    }

    public boolean verifyPassword(String password, String hash) {
        return encoder.matches(password, hash);
    }
}
