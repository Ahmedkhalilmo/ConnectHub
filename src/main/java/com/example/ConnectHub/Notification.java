package com.example.ConnectHub;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Notification implements Serializable{

    protected String message;
    LocalDateTime timestamp;
    User sender;


    public User getSender() {
        return sender;
    }

    public Notification(String message, User sender) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.sender = sender;
    }

    public abstract String getMessage();

    public abstract LocalDateTime getTimestamp() ;
}
