package com.learn.stomp.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionRegistryService {

    private final Map<String, String> sessionToUser = new ConcurrentHashMap<>();

    public void registerUser(String sessionId, String username) {
        this.sessionToUser.put(sessionId, username);
    }

    public String removeUser(String sessionId) {
        return this.sessionToUser.remove(sessionId);
    }
}
