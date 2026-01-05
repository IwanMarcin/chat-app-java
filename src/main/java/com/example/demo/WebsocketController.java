package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final WebSocketSessionManager webSocketSessionManager;

    @Autowired
    public WebsocketController(SimpMessagingTemplate simpMessagingTemplate, WebSocketSessionManager webSocketSessionManager) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.webSocketSessionManager = webSocketSessionManager;
    }

    @MessageMapping("/message")
    public void processMessage(Message message) {
        //just for debugging
        System.out.println("Incoming message from " + message.getUser() + ": " + message.getMessage());

        simpMessagingTemplate.convertAndSend("/topic/messages", message);

        //just for debugging
        System.out.println("Sent message to /topic/messages: from " + message.getUser() + ": " + message.getMessage());
    }

    @MessageMapping("/connect")
    public void connectUser(String username) {
        webSocketSessionManager.addUsername(username);
        webSocketSessionManager.broadcastActiveUsernames();
        System.out.println(username + " connected");
    }

    @MessageMapping("/disconnect")
    public void disconnectUser(String username) {
        webSocketSessionManager.removeUsername(username);
        webSocketSessionManager.broadcastActiveUsernames();
        System.out.println(username + " disconnected");
    }
}
