package com.example.ConnectHub;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class Friend extends Profile {
    @FXML
    public VBox ProfilePostsContainer1;
    private User friendUser = ProfilePage.friendUser;

    public Friend() {
        super();
    }

    public void initialize() {
        loadprofileposts(friendUser);
        displayPosts(ProfilePostsContainer1);
        nameLabel.setText(friendUser.getUsername());
        Image image = new Image(getClass().getResourceAsStream(friendUser.getImageUrl()));
        CircleImageView.setFill(new ImagePattern(image));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDateString = friendUser.getUserBirthDate().format(formatter);

        BirthDateL.setText(formattedDateString);
        GenderL.setText(friendUser.getGender());
        EmailL.setText(friendUser.getEmail());

        FriendsManager friendsManager = new FriendsManager();
        List<String> Userfriends = friendsManager.getUserFriends(friendUser.getUsername());
        List<String> Myfriends = friendsManager.getUserFriends(myUser.getUsername());
        Myfriends.retainAll(Userfriends);
        Userfriends.removeIf(friend -> friend.equals(friendUser.getUsername()) || friend.equals(myUser.getUsername()));
        MutualfriendsListView.getItems().addAll(Userfriends);
        MutualfriendsListView.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        MutualfriendsListView.setCellFactory(param -> new ListCell<String>() {
            private final HBox hbox = new HBox();
            private final ImageView imageView = new ImageView();
            private final Label label = new Label();

            {
                hbox.setSpacing(10);
                imageView.setFitHeight(40);
                imageView.setFitWidth(40);
                hbox.getChildren().addAll(imageView, label);
            }

            @Override
            protected void updateItem(String username, boolean empty)
            {
                super.updateItem(username, empty);

                if (empty || username == null) {
                    setGraphic(null);
                } else {
                    User friend = UserManager.getFriend(username); // Assuming this retrieves friend info
                    Image image = new Image(getClass().getResourceAsStream(friend.getImageUrl()));
                    imageView.setImage(image);
                    label.setText(username);
                    setGraphic(hbox);
                }
            }
        });
    }

    public void returntoHomepage(MouseEvent e) throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("ProfilePage.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("profile.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();



    }


}