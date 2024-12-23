package com.example.ConnectHub;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FriendRequestNotification extends Notification implements Serializable{
   public FriendRequestNotification(String message, User sender)
    {

        super(message,sender, 1);
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}
