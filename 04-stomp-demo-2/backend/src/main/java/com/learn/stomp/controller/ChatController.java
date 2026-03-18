package com.learn.stomp.controller;

import com.learn.stomp.model.ChatMessage;
import com.learn.stomp.model.PresenceMessage;
import com.learn.stomp.service.PresenceService;
import com.learn.stomp.service.SessionRegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final PresenceService presenceService;
    private final SessionRegistryService registryService;

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessage chatMessage, @Header("simpSessionId") String sessionId) {
        String username = chatMessage.getSender();
        this.registryService.registerUser(sessionId, username);
        this.presenceService.addUser(username);

        ChatMessage joinMessage = ChatMessage.builder()
                .type("JOIN")
                .sender(username)
                .recipient(null)
                .content(username + "joined the chat")
                .timestamp(System.currentTimeMillis())
                .build();

        this.messagingTemplate.convertAndSend("/topic/public", joinMessage);
        this.messagingTemplate.convertAndSend(
                "/topic/presence",
                new PresenceMessage(this.presenceService.getOnlineUsers())
        );
    }

    @MessageMapping("/chat.send")
    public void sendPublicMessage(@Payload ChatMessage message) {
        ChatMessage outgoingMessage = ChatMessage.builder()
                .type("CHAT")
                .sender(message.getSender())
                .recipient(null)
                .content(message.getContent())
                .timestamp(System.currentTimeMillis())
                .build();
        this.messagingTemplate.convertAndSend("/topic/public", outgoingMessage);
    }

    @MessageMapping("/chat.private")
    public void sendPrivateMessage(@Payload ChatMessage message, Principal principal) {
        System.out.println("principal = " + (principal != null ? principal.getName() : "null"));
        System.out.println("sender = " + message.getSender());
        System.out.println("recipient = " + message.getRecipient());

        String sender = principal != null ? principal.getName() : message.getSender();

        ChatMessage outgoingMessage = ChatMessage.builder()
                .type("PRIVATE")
                .sender(sender)
                .recipient(message.getRecipient())
                .content(message.getContent())
                .timestamp(System.currentTimeMillis())
                .build();
        this.messagingTemplate.convertAndSendToUser(message.getRecipient(), "/queue/private", outgoingMessage);
        this.messagingTemplate.convertAndSendToUser(sender, "/queue/private", outgoingMessage);
    }

    @MessageMapping("/chat.typing")
    public void typing(@Payload ChatMessage message) {
        ChatMessage outgoingMessage = ChatMessage.builder()
                .type("TYPING")
                .sender(message.getSender())
                .recipient(message.getRecipient())
                .content("")
                .timestamp(System.currentTimeMillis())
                .build();
        this.messagingTemplate.convertAndSend("/topic/typing", outgoingMessage);
    }
}
