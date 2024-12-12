package com.example.ConnectHub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;

public class Home {
    private Stage stage;
    private Scene scene;
    private User user=UserManager.curr_user;

    public void setUser() {

        System.out.println(user.getUsername());


    }
    public void OpenChat(MouseEvent e) throws IOException {
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(this.getClass().getResource("Chat.fxml"));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        // Debugging
        System.out.println("Navigated to Chat.fxml");
    }

    public void OpenChat(ActionEvent e) throws IOException {
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(this.getClass().getResource("Chat.fxml"));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}

