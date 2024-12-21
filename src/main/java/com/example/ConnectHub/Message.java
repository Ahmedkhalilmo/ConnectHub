package com.example.ConnectHub;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.Serializable;

public class Message extends Text implements Serializable {
    private String currentDateTime;


    public Message( User sender, String content) {
        super(content,sender);
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDate.format(formatter);
        this.currentDateTime = formattedDateTime;
    }
    public  String getContent(){
        return content;
    }
    public  User getUser(){
        return user;
    }
    public String getCurrentDateTime() {
        return currentDateTime;
    }
}