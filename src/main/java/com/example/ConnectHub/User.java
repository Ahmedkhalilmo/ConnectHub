package com.example.ConnectHub;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String username;
    private String password;
    private String email;
    private String gender;
    private LocalDate userBirthDate;
    private String imageUrl;

    public static List<Conversation> chats = new ArrayList<>();

    public User() {}

    public User(String username, String password, String email, String gender, LocalDate userBirthDate, String imageUrl) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.userBirthDate = userBirthDate;
        this.imageUrl = imageUrl;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getUserBirthDate() {
        return userBirthDate;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
