package com.example.ConnectHub;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ReactNotification extends Notification implements Serializable{
    public ReactNotification(String message, User sender, int type)
    {

        super(message,sender, type);
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}

