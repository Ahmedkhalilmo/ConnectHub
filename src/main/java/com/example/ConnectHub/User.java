package com.example.ConnectHub;

import java.io.Serializable;
import java.time.LocalDate;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String email;
    private String gender;
    private LocalDate userBirthDate;
    private String imageUrl;

    public User() {}

    public User(String username, String password, String email, String gender, LocalDate userBirthDate, String imageUrl) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.userBirthDate = userBirthDate;
        this.imageUrl = imageUrl;
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
