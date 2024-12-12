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
    private List<Notification> CurrentNotifications = new ArrayList<>();
    public List<Notification> getCurrentNotifications() {
        return CurrentNotifications;
    }
    public void RemoveNotification(Notification notification) {
        CurrentNotifications.remove(notification);
    }
    //****
    public void addNotifications(Notification newNotification) {
        CurrentNotifications.add(newNotification);
    }
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


    Object getNotifications() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
