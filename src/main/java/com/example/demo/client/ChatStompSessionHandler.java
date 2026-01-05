package com.example.demo.client;

import com.example.demo.Message;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

public class ChatStompSessionHandler extends StompSessionHandlerAdapter {
    private String username;

    public ChatStompSessionHandler(String username){
        this.username = username;
    }

    @Override
    public void afterConnected(StompSession stompSession, StompHeaders connectedHeaders) {
        System.out.println("User" + username + " connected"); // for debugging
        stompSession.send("/app/connect", username);

        stompSession.subscribe("/topic/messages", new StompFrameHandler() {
            // method to inform about the expected type of payload (data sent to the destination)
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Message.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                    if (payload instanceof Message message) {
                        System.out.println("Received message from " + message.getUser() + ": " + message.getMessage());
                    } else {
                        System.out.println("Received unexpected payload of type: " + payload.getClass().getSimpleName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        System.out.println("User" + username + " joined to /topic/messages"); // for debugging

    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable exception) {
        exception.printStackTrace();
    }
}
