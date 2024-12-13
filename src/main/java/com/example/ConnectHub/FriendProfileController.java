package com.example.ConnectHub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
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

public class FriendProfileController {
    private Stage stage;
    private Scene scene;
    private User user = SearchResults.friendUser;
    FriendsManager friends_manager = new FriendsManager();

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
    public void addFriend(ActionEvent e){
        String user2 = user.getUsername();
        String user1 = UserManager.curr_user.getUsername();
        friends_manager.addFriend(user1,user2);
    }

}