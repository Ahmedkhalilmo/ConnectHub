package com.example.ConnectHub;

import com.example.ConnectHub.FriendsManager;

import javafx.geometry.Rectangle2D;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ProfilePage {
    private Stage stage;
    private Scene scene;
    private User user = UserManager.curr_user;

    @FXML
    private Label nameLabel;

    @FXML
    private Circle CircleImageView;

    @FXML
    private Label BirthDateL;

    @FXML
    private Label GenderL;

    @FXML
    private Label EmailL;

    public void initialize() {
        nameLabel.setText(user.getUsername());
        Image image = new Image(getClass().getResourceAsStream(user.getImageUrl()));
        CircleImageView.setFill(new ImagePattern(image));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDateString = user.getUserBirthDate().format(formatter);

        BirthDateL.setText(formattedDateString);
        GenderL.setText(user.getGender());
        EmailL.setText(user.getEmail());
    }

    public void returntoHomepage(MouseEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("Home.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Home.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    public void LogOut(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("login.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene=new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Login.css")).toExternalForm());
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

}
