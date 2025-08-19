package com.abhiroop.chatbackend.service;

import org.hibernate.validator.internal.constraintvalidators.hv.URLValidator;
import org.springframework.stereotype.Service;

@Service
public class UrlValidatorService {
    final URLValidator urlValidator = new URLValidator();

    public boolean isValid(String url) {
        return urlValidator.isValid(url, null);
    }
}
