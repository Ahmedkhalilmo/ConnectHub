package com.example.ConnectHub;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.Objects;

public class Login {
    private Stage stage;
    private Scene scene;
    @FXML
    private TextField username;
    @FXML
    Label ErrorLabel;
    @FXML
    private PasswordField password;

    public void setLogin(ActionEvent e) throws IOException  {
        String userNameValue = username.getText();
        String passwordValue = password.getText();
        User user = UserManager.getUsers(userNameValue,passwordValue);
        if(user != null)
        {
            UserManager.loadNotifications();
            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(this.getClass().getResource("Home.fxml"));
            scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Home.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            // Calculate center position
            double centerX = (screenBounds.getWidth() - stage.getWidth()) / 2;
            double centerY = (screenBounds.getHeight() - stage.getHeight()) / 2;

            // Apply the position
            stage.setX(centerX);
            stage.setY(centerY);

        }
        else
        {
            ErrorLabel.setText("Username or password is incorrect");
            System.out.println("not a user");
        }

    }
    public void setCreateAccount(ActionEvent e) throws IOException  {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("CreateAccount.fxml")));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene=new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("CreateAccount.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

}
