package com.example.demo.client;

import com.example.demo.Message;

import java.util.ArrayList;

public interface MessageListener {
    void onMessageRecieve(Message message);
    void onActiveUsersUpdate(ArrayList<String> activeUsers);
}
