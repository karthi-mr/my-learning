package com.learn.backend.websocket;

import com.learn.backend.dto.ChatDto;
import org.jspecify.annotations.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("Client connected: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String clientMessage = message.getPayload();
        System.out.printf("Received from client in %s: %s%n", session.getId(), clientMessage);
        for (WebSocketSession wsSession: sessions) {
            if (wsSession.isOpen()) {
                wsSession.sendMessage(new TextMessage(String.format("User (%s): %s", wsSession.getId(), clientMessage)));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.printf("Client disconnected with session-id: %s with status: %s\n",
                session.getId(), status.getReason());
    }
}
