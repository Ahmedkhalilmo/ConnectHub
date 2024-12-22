package com.example.ConnectHub;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class FriendsListGUI {
    @FXML
    Label ErrorLabel;
    private Stage stage;
    private Scene scene;
    @FXML
    VBox FriendVBox;
    FriendsManager friendsManager = new FriendsManager();
    User curruser = UserManager.curr_user;
    @FXML
    Parent FriendPanel;

    @FXML

    public void returnToConversation(MouseEvent e) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Conversation.fxml"));
        Parent root = loader.load();

        ConversationPanel conversationPanel = loader.getController();
        conversationPanel.setData(curr_conversation);

        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Chat.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    Conversation curr_conversation;

    public void setData(Conversation conversation, Parent FriendPanel) throws IOException {
        this.FriendPanel = FriendPanel;
        this.curr_conversation = conversation;
        loadFriends();
    }

    private void loadFriends() throws IOException {
        List<String> friends = friendsManager.getUserFriends(curruser.getUsername());
        if (friends.isEmpty()) {
            ErrorLabel.setText("You dont have any friends");
        } else {
            for (String username : friends) {
                User user = UserManager.getFriend(username);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("friend_list.fxml"));
                Parent root = loader.load();

                FriendPanel friendPanel = loader.getController();
                friendPanel.setData(user, curr_conversation, FriendVBox, root, true);

                FriendVBox.getChildren().add(root);
                System.out.println("Friend loaded: " + user.getUsername());
            }
        }

    }
}