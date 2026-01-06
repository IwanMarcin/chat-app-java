package com.example.demo.client;

import com.example.demo.Message;
import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class ClientGUI extends JFrame {
    public ClientGUI(String username){
        super("User: " + username);

        setSize(1280, 720);
        setLocationRelativeTo(null);

    }
}