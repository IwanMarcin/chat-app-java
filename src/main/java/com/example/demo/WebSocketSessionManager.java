package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketSessionManager {
    private final Set<String> activeUsernames = ConcurrentHashMap.newKeySet();
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketSessionManager(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void addUsername(String username) {
        activeUsernames.add(username);
    }

    public void removeUsername(String username) {
        activeUsernames.remove(username);
    }

    public void broadcastActiveUsernames() {
        simpMessagingTemplate.convertAndSend("/topic/users", activeUsernames);
        System.out.println("Broadcasting active users to /topic/users " + activeUsernames);
    }

}











