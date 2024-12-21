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

public class FriendProfileController {
    private Stage stage;
    private Scene scene;
    private User user = SearchResults.friendUser;
    private User Myuser = UserManager.curr_user;

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

    @FXML
    private ListView<String> MutualfriendsListView;

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
        List<String> Userfriends = friendsManager.getUserFriends(user.getUsername());
        List<String> Myfriends = friendsManager.getUserFriends(Myuser.getUsername());
        Userfriends.retainAll(Myfriends);
        Userfriends.removeIf(friend -> friend.equals(user.getUsername()) || friend.equals(Myuser.getUsername()));
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
        User user2 = user;
        User user1 = UserManager.curr_user;
        friends_manager.addFriend(user1,user2);
    }

}