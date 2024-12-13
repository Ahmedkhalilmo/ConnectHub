package com.example.ConnectHub;

import java.io.Serializable;

public abstract class Text implements Serializable {
    public String content;
    public User user;

    public Text(String content, User user) {
        this.content = content;
        this.user = user;
    }

    public abstract String getContent();

    public abstract User getUser();
    public void editContent(String newContent) {
        this.content = newContent;
    }

}