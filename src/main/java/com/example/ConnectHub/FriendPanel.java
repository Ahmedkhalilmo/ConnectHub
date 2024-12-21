package com.example.ConnectHub;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class FriendPanel {
    @FXML
    Label usernameTF;
    @FXML
    Circle ProfileImageView;
    Conversation curr_conversation;
    User user;
    VBox friendVBox;
    Parent friendPanel;
    Boolean isChat;
    public void setData(User user, Conversation conversation, VBox friendVBox, Parent root,boolean isChat)
    {
        this.isChat = isChat;
        this.friendPanel = root;
        this.friendVBox = friendVBox;
        this.curr_conversation = conversation;
        this.user = user;
        usernameTF.setText(user.getUsername());
        Image image = new Image(getClass().getResourceAsStream(user.getImageUrl()));
        ProfileImageView.setFill(new ImagePattern(image));
    }

    public void addToConversation(MouseEvent e) throws IOException {
        if (!curr_conversation.getUsersOfConversation().contains(user)) {
            curr_conversation.addUser(user);  // Add user to conversation
            UserManager.saveChats();  // Save the updated chat list
            friendVBox.getChildren().remove(friendPanel);  // Remove the current panel from the friend list

            // Optionally refresh the conversation view or reload the conversation screen

            System.out.println(user.getUsername() + " added to the conversation.");
        } else {
            System.out.println(user.getUsername() + " is already in the conversation.");
        }
    }
}