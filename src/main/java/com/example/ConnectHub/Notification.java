package com.example.ConnectHub;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Notification implements Serializable{

    protected String message;
    LocalDateTime timestamp;
    User sender;
    int type;
    // 1 -> Friend Request
    // 2 -> Like
    // 3 -> Comment

    public User getSender() {
        return sender;
    }

    public Notification(String message, User sender, int type) {
        this.message = message;
        this.sender = sender;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    public abstract String getMessage();

    public abstract LocalDateTime getTimestamp() ;
}
