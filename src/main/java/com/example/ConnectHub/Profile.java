package com.example.ConnectHub;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public abstract class Profile {
    protected Stage stage;
    protected Scene scene;
    protected User myUser = UserManager.curr_user;

    @FXML
    protected Label nameLabel;

    @FXML
    protected Circle CircleImageView;

    @FXML
    protected Label BirthDateL;

    @FXML
    protected Label GenderL;

    @FXML
    protected Label EmailL;

    @FXML
    protected ListView<String> friendsListView;
    @FXML
    protected ListView<String> MutualfriendsListView;

    protected abstract void initialize() ;

    protected  void switchToFriendProfile() {};

    protected abstract void returntoHomepage(MouseEvent e) throws IOException ;


    protected void LogOut(javafx.event.ActionEvent actionEvent) throws IOException {};
}
