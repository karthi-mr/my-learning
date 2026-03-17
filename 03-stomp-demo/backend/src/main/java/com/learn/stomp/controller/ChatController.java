package com.learn.stomp.controller;

import com.learn.stomp.model.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ChatController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        log.info("Receiving m: {}", message );
        return message;
    }

    @MessageMapping("/chat1")
    @SendTo("/topic/messages1")
    public ChatMessage sendMessage1(ChatMessage message) {
        log.info("Receiving m1: {}", message);
        return message;
    }
}
