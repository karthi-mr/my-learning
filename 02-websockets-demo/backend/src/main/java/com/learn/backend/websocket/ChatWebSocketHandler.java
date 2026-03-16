package com.learn.backend.websocket;

import com.learn.backend.dto.ChatDto;
import org.jspecify.annotations.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("Client connected: " + session.getId());
        for (WebSocketSession wsSession: sessions) {
            if (!wsSession.getId().equals(session.getId()) && wsSession.isOpen()) {
                wsSession.sendMessage(new TextMessage("User with session: " + session.getId() + " joined."));
            }
        }
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        String clientMessage = message.getPayload();
        var response = new ChatDto(session.getId(), clientMessage, LocalDateTime.now());
        for (WebSocketSession wsSession: sessions) {
            if (!wsSession.getId().equals(session.getId()) && wsSession.isOpen()) {
                wsSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
            }
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, CloseStatus status) throws Exception {
        System.out.printf("Client disconnected with session-id: %s with status: %s\n",
                session.getId(), status.getReason());
        for (WebSocketSession wsSession: sessions) {
            if (!wsSession.getId().equals(session.getId()) && wsSession.isOpen()) {
                wsSession.sendMessage(new TextMessage("User with session: " + session.getId() + " disconnected."));
            }
        }
        sessions.remove(session);
    }
}
