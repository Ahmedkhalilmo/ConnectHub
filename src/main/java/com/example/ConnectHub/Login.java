package com.example.ConnectHub;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
        UserManager user=new UserManager();
       if(! user.userFound(userNameValue)){
           ErrorLabel.setText("username or password incorrect");
           return;
       }
        user.UserData(userNameValue);
        if(passwordValue.equals(user.password)){
            Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("Home.fxml")));
            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            scene=new Scene(root);
            Home h=new Home();
            h.setUser(user);
           // scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Home.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
        else{
            ErrorLabel.setText("username or password incorrect");


        }

    }
    public void setCreatAccout(ActionEvent e) throws IOException  {
        Parent root = FXMLLoader.load(this.getClass().getResource("CreatAccout.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene=new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("CreatAccout.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
