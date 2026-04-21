package com.learn.backend.auth;

public record AuthResponse(
        String accessToken,

        String refreshToken,

        String tokenType
) {
}
