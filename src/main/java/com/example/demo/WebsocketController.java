package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebsocketController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/message")
    public void processMessage(Message message) {
        //just for debugging
        System.out.println("Incoming message from " + message.getUser() + ": " + message.getMessage());

        simpMessagingTemplate.convertAndSend("/topic/message", message);

        //just for debugging
        System.out.println("Sent message from " + message.getUser() + ": " + message.getMessage());
    }
}
