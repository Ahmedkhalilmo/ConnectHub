package com.example.ConnectHub;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchResults {
    private Stage stage;
    private Scene scene;
    public static List<User> usersearch = new ArrayList<>();
    public static User friendUser;
    public static boolean isEmpty = false;

    @FXML
    private ListView<User> listView;
    @FXML
    private Label noFriends;

    public void initialize() {
        ObservableList<User> users = FXCollections.observableArrayList(usersearch);
        listView.setItems(users);

        if (isEmpty) {
            noFriends.setText("User not found");
            isEmpty = false;
        } else {
            noFriends.setVisible(false);
        }

        listView.setCellFactory(param -> new ListCell<User>() {
            private final ImageView imageView = new ImageView();
            private final Label label = new Label();

            {
                imageView.setFitHeight(40);
                imageView.setFitWidth(40);
                label.setStyle("-fx-padding: 10;");
            }


            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(user.getImageUrl()))));
                    label.setText(user.getUsername());
                    setGraphic(new HBox(imageView, label));
                }
            }
        });

        listView.setOnMouseClicked(this::onUserClick);
    }

    private void onUserClick(MouseEvent event) {
        User selectedUser = listView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            System.out.println("Selected user: " + selectedUser.getUsername());
            friendUser = selectedUser;
            switchToFriendProfile();
        } else {
            System.out.println("No user selected!");
        }
    }

    private void switchToFriendProfile() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("FriendProfileController.fxml"));
            Parent root = loader.load();
            stage = (Stage) listView.getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(ProfilePage.isDarkTheme ?"Darkprofile.css":"profile.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void returntoHomepage(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Home.fxml")));
        Parent root = loader.load();
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(ProfilePage.isDarkTheme ?"DarkHome.css":"Home.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
