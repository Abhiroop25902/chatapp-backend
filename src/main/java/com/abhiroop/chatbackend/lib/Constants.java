package com.abhiroop.chatbackend.lib;

import java.util.List;

public class Constants {
    public static final String SCHEMA_NAME = "chat_app";
    public static final String USER_TABLE_NAME = "users";
    public static final String REFRESH_TOKENS_TABLE_NAME = "refresh_tokens";

    public static final int MESSAGE_PAGE_SIZE = 50;

    public static final List<String> JWT_CHECK_EXCLUDED_PATHS = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/health"
    );

    private Constants() {
    }
}
