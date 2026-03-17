package com.learn.stomp.model;

public record ChatMessage(
        String sender,

        String content
) {
}
