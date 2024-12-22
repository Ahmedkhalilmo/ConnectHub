package com.example.ConnectHub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class FriendProfileController extends Profile {
    @FXML
    private Button connectButton;
    @FXML
    public VBox ProfilePostsContainer2;
    private User friendUser = SearchResults.friendUser;

    public FriendProfileController() {
        super();
    }

    public void initialize() {
        loadprofileposts(friendUser);
        displayPosts(ProfilePostsContainer2);
        nameLabel.setText(friendUser.getUsername());
        Image image = new Image(getClass().getResourceAsStream(friendUser.getImageUrl()));
        CircleImageView.setFill(new ImagePattern(image));
        updateConnectButton();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDateString = friendUser.getUserBirthDate().format(formatter);

        BirthDateL.setText(formattedDateString);
        GenderL.setText(friendUser.getGender());
        EmailL.setText(friendUser.getEmail());

        List<String> Userfriends = FriendsManager.getUserFriends(friendUser.getUsername());
        List<String> Myfriends = FriendsManager.getUserFriends(myUser.getUsername());
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

    private void updateConnectButton() {
        if(UserManager.hasPendingRequest(UserManager.curr_user, friendUser)) {
            connectButton.setText("Pending");
            connectButton.setStyle("-fx-background-color: grey; -fx-text-fill: white;");
            connectButton.setDisable(true);
        }
        else if(UserManager.hasPendingRequest(friendUser, UserManager.curr_user)) {
            connectButton.setText("Accept");
            connectButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            connectButton.setOnAction(event -> acceptConnection());
        }
        else if(FriendsManager.getUserFriends(UserManager.curr_user.getUsername()).contains(friendUser.getUsername())) {
            connectButton.setText("Connected");
            connectButton.setStyle("-fx-background-color: grey; -fx-text-fill: white;");
            connectButton.setDisable(true);
        }
        else {
            connectButton.setText("Connect");
            connectButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
            connectButton.setOnAction(event -> sendConnectionRequest());
        }
    }

    private void acceptConnection() {
        FriendsManager.acceptRequest(friendUser, UserManager.curr_user);
        System.out.println(friendUser.getUsername() + "sent" +  UserManager.curr_user.getUsername());
        List<Notification> lst = UserManager.notifications.get( UserManager.curr_user);
        for(Notification n : lst) {
            if(n.sender.getUsername().equals(friendUser.getUsername()) && n instanceof FriendRequestNotification) {
                UserManager.removeNotification(myUser,n);
                break;
            }
        }
        updateConnectButton();
    }

    private void sendConnectionRequest() {
        FriendsManager.addFriend(myUser, friendUser);
        updateConnectButton();
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
        FriendsManager.addFriend(user1,user2);
        updateConnectButton();
    }
}