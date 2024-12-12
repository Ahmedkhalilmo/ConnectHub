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

    public void OpenChat(javafx.scene.input.MouseEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("Chat.fxml")));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene=new Scene(root);
//        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("CreateAccount.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();

    }
}

