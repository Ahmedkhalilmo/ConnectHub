package com.example.ConnectHub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        UserManager.loadUsers();
        Scene scene = new Scene(root);
        try {
            Image icon = new Image(getClass().getResourceAsStream("icon.jpg"));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        scene.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());
        stage.setTitle("ConnectHub");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}