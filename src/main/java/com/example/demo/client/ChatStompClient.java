package com.example.demo.client;

import com.example.demo.Message;
import org.springframework.messaging.converter.JacksonJsonMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChatStompClient {
    private StompSession stompSession;
    private String username;

    ChatStompClient(String username) throws ExecutionException, InterruptedException {
        this.username = username;

        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));

        SockJsClient sockJsClient = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new JacksonJsonMessageConverter());

        StompSessionHandler sessionHandler = new ChatStompSessionHandler(username);
        String url = "ws://localhost:8080/ws"; // /ws comes from registerStompEndpoints from the WebsocketConfig file

        stompSession = stompClient.connectAsync(url, sessionHandler).get();
    }

    public void sendMessage(Message message) {
        try {
            stompSession.send("/app/message", message);
            System.out.println("Message sent: from " + message.getUser() + ": " + message.getMessage()); // for debugging;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnectUser(String username) {
        stompSession.send("/app/disconnect", username);
        System.out.println("Disconnect User: " + username);
    }
}
