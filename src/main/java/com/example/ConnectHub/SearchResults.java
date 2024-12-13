package com.example.ConnectHub;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
    private ListView<String> listView;
    @FXML
    private Label noFriends;

    public void initialize() {
        ObservableList<String> usernames = FXCollections.observableArrayList();
        for (User user : usersearch) {
            usernames.add(user.getUsername());
        }

        listView.setItems(usernames);
        if(isEmpty) {
            noFriends.setText("User not found");
            isEmpty = false;
        }
        else {
            noFriends.setVisible(false);

        }
        listView.setOnMouseClicked(this::onUserClick);
    }


    private void onUserClick(MouseEvent event) {
        String selectedUsername = listView.getSelectionModel().getSelectedItem();

        if (selectedUsername != null) {

            for (User user : usersearch) {
                if (user.getUsername().equals(selectedUsername)) {
                    friendUser = user;
                    break;
                }
            }

            switchToFriendProfile();
        }
    }

    private void switchToFriendProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FriendProfileController.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) listView.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
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
}