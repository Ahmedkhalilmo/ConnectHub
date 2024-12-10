package com.example.ConnectHub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.Objects;

public class Home {

    private User user;

    public void setUser(User user) {
        this.user = user;
        System.out.println(user.getUsername());


    }


}


