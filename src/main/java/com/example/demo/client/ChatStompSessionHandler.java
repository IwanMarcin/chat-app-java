package com.example.demo.client;

import com.example.demo.Message;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ChatStompSessionHandler extends StompSessionHandlerAdapter {
    private String username;
    private MessageListener messageListener;

    public ChatStompSessionHandler(MessageListener messageListener, String username) {
        this.username = username;
        this.messageListener = messageListener;
    }

    @Override
    public void afterConnected(StompSession stompSession, StompHeaders connectedHeaders) {
        System.out.println("User " + username + " connected"); // for debugging

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
                        messageListener.onMessageRecieve(message);
                        System.out.println("Received message from " + message.getUser() + ": " + message.getMessage());
                    } else {
                        System.out.println("Received unexpected payload of type: " + payload.getClass().getSimpleName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        System.out.println("User " + username + " joined to /topic/messages"); // for debugging

        stompSession.subscribe("/topic/users", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ArrayList.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try{
                    if(payload instanceof ArrayList){
                        @SuppressWarnings("unchecked")
                        ArrayList<String> activeUsers = (ArrayList<String>) payload;
                        messageListener.onActiveUsersUpdate(activeUsers);
                        System.out.println("Received active users: " + activeUsers);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        System.out.println("User " + username + " joined to /topic/users"); // for debugging

        stompSession.send("/app/connect", username);
        stompSession.send("/app/request-users", "");
    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable exception) {
        exception.printStackTrace();
    }
}
