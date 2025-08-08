package com.abhiroop.chatbackend.lib;

import java.util.List;

public class UnauthenticatedPaths {
    public static final List<String> EXCLUDED_PATHS = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/health"
    );

    private UnauthenticatedPaths() {
    }
}
