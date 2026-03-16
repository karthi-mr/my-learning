package com.learn.backend.dto;

import java.time.LocalDateTime;

public record ChatDto(
        String session,

        String message,

        LocalDateTime time
) {
}
