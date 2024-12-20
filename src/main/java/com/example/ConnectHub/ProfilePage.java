package com.example.ConnectHub;

import javafx.geometry.Rectangle2D;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class ProfilePage {
    private Stage stage;
    private Scene scene;
    private User user = UserManager.curr_user;
    public static User friendUser;

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

    @FXML
    private ListView<String> friendsListView;

    public void initialize() {
        nameLabel.setText(user.getUsername());
        Image image = new Image(getClass().getResourceAsStream(user.getImageUrl()));
        CircleImageView.setFill(new ImagePattern(image));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDateString = user.getUserBirthDate().format(formatter);

        BirthDateL.setText(formattedDateString);
        GenderL.setText(user.getGender());
        EmailL.setText(user.getEmail());

        FriendsManager friendsManager = new FriendsManager();
        List<String> friends = friendsManager.getUserFriends(user.getUsername());
        friendsListView.getItems().addAll(friends);
        friendsListView.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        friendsListView.setOnMouseClicked(this::onUserClick);
    }

    private void onUserClick(MouseEvent event) {
        String selectedUsername = friendsListView.getSelectionModel().getSelectedItem();
        if (selectedUsername != null) {
            System.out.println("Selected user: " + selectedUsername);
            friendUser = UserManager.getFriend(selectedUsername);
            switchToFriendProfile();
        }
    }

    private void switchToFriendProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FriendProfileController.fxml"));            Parent root = loader.load();
            stage = (Stage) friendsListView.getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("profile.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Login.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double centerX = (screenBounds.getWidth() - stage.getWidth()) / 2;
        double centerY = (screenBounds.getHeight() - stage.getHeight()) / 2;
        stage.setX(centerX);
        stage.setY(centerY);
    }
}
