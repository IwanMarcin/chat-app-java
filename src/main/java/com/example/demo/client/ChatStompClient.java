package com.example.demo.client;

import org.springframework.messaging.converter.JacksonJsonMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
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
    private ChatStompClient(String username) throws ExecutionException, InterruptedException {
        this.username = username;

        List<Transport> transports = new ArrayList<Transport>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));

        SockJsClient sockJsClient = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new JacksonJsonMessageConverter());

        ChatStompSessionHandler chatStompSessionHandler = new ChatStompSessionHandler(username);
        String url = "ws://localhost:8080/ws"; // /ws comes from registerStompEndpoints from the WebsocketConfig file

        stompSession = stompClient.connectAsync(url, chatStompSessionHandler).get();
    }
}
