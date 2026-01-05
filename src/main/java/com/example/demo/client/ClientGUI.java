package com.example.demo.client;

import com.example.demo.Message;

import java.util.concurrent.ExecutionException;

public class ClientGUI {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ChatStompClient chatStompClient = new ChatStompClient("LubieJave");
        chatStompClient.sendMessage(new Message("Hello from LubieJave!", "LubieJave"));
        chatStompClient.disconnectUser("LubieJave");

        Thread.sleep(2000); // for debugging

    }
}