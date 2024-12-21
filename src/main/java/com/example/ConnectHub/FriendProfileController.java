package com.example.ConnectHub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
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

public class FriendProfileController extends Profile {

    private User friendUser= SearchResults.friendUser;
    FriendsManager friends_manager = new FriendsManager();


    public void initialize() {


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
        Userfriends.retainAll(Myfriends);
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

            Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("Home.fxml")));
            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Home.css")).toExternalForm());
            stage.setScene(scene);


    }
    public void addFriend(ActionEvent e){
        User user2 =friendUser;
        User user1 = myUser;
        friends_manager.addFriend(user1,user2);
    }

}