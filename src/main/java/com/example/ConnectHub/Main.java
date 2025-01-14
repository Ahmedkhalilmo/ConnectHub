package com.example.ConnectHub;

import javafx.geometry.Rectangle2D;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        UserManager.loadUsers();
        UserManager.loadNotifications();
        Home.loadPostsFromFile();
        FriendsManager.loadFromFile();
        CommentsManager.loadCommentsFromFile();


        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
        try {
            Image icon = new Image(getClass().getResourceAsStream("icon.png"));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(ProfilePage.isDarkTheme?"DarkLogin.css":"Login.css")).toExternalForm());
        stage.setTitle("ConnectHub");
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double centerX = (screenBounds.getWidth() - stage.getWidth()) / 2;
        double centerY = (screenBounds.getHeight() - stage.getHeight()) / 2;
        stage.setX(centerX);
        stage.setY(centerY);
        stage.setOnCloseRequest(event ->
        {
            event.consume();
            UserManager.saveUsers();
            FriendsManager.saveToFile();
            CommentsManager.saveCommentsToFile();
            Home.savePostsToFile();
            UserManager.saveNotifications();

                stage.close();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}