package com.learn.stomp.service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PresenceService {

    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();

    public void addUser(String username) {
        if (username != null && !username.isBlank()) {
            this.onlineUsers.add(username);
        }
    }

    public void removeUser(String username) {
        if (username != null && !username.isBlank()) {
            this.onlineUsers.remove(username);
        }
    }

    public Set<String> getOnlineUsers() {
        return Set.copyOf(this.onlineUsers);
    }
}
