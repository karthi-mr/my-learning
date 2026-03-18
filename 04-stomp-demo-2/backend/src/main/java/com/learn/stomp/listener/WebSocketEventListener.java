package com.learn.stomp.listener;

import com.learn.stomp.model.ChatMessage;
import com.learn.stomp.model.PresenceMessage;
import com.learn.stomp.service.PresenceService;
import com.learn.stomp.service.SessionRegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final PresenceService presenceService;
    private final SessionRegistryService registryService;

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        String username = this.registryService.removeUser(sessionId);

        if (username != null) {
            this.presenceService.removeUser(username);

            ChatMessage leaveMessage = ChatMessage.builder()
                    .type("LEAVE")
                    .sender(username)
                    .recipient(null)
                    .content(username + " left the chat")
                    .timestamp(System.currentTimeMillis())
                    .build();
            this.messagingTemplate.convertAndSend("/topic/public", leaveMessage);
            this.messagingTemplate.convertAndSend(
                    "/topic/presence",
                    new PresenceMessage(this.presenceService.getOnlineUsers())
            );
        }
    }
}
