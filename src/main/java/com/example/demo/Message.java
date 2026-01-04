package com.example.demo;

public class Message {

    private String message;
    private String user;

    public Message() {}

    public Message(String message, String user) {
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }
    public String getUser() {
        return user;
    }
}
