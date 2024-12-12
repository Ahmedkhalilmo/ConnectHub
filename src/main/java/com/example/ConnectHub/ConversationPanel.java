package com.example.ConnectHub;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class ConversationPanel {

    private Stage stage;
    private Scene scene;
    @FXML
    VBox MessagesVBox;
    @FXML
    Label ChatnameL;
    @FXML
    TextField MessageContentTF;
    Conversation curr_conversation;

    public void setData(Conversation conversation) throws IOException {
        curr_conversation = conversation;
        ChatnameL.setText(conversation.getNameOfChat());
        loadMsgs();
    }

    private void loadMsgs() throws IOException {
        for (Message message : curr_conversation.getAllMessages()) {
            createMsgPanel(message);
        }
    }

    private void createMsgPanel(Message message) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        BorderPane root;
        fxmlLoader.setLocation(getClass().getResource("/com/example/ConnectHub/Messages.fxml")); // Corrected path
        root = fxmlLoader.load();

// Get the controller
        MessagePanel chatPanel = fxmlLoader.getController();
        if (chatPanel != null) {
            chatPanel.setData(message); // Pass the message to the controller for handling
        }

// Dynamically modify alignment based on sender
        if (message.getUser().getUsername().equalsIgnoreCase(UserManager.curr_user.getUsername())) {
            BorderPane.setAlignment(root, Pos.CENTER_RIGHT); // Right align for current user
            root.setPadding(new Insets(25, 25, 25, 125)); // Add margin as required
        } else {
            BorderPane.setAlignment(root, Pos.CENTER_LEFT); // Left align for others
            root.setPadding(new Insets(25, 125, 25, 25)); // Adjust margin for other user
        }

// Add the new message to the VBox
        MessagesVBox.getChildren().add(root);
    }

    public void createNewMsg(MouseEvent e) throws IOException {
        Message message = new Message(UserManager.curr_user, MessageContentTF.getText());
        curr_conversation.sendMessage(UserManager.curr_user, message);
        createMsgPanel(message);
        MessageContentTF.clear();
    }

    //public void addUser(MouseEvent e) throws IOException {
    //StackPane StartUpPane = (StackPane)((Node)e.getSource()).getScene().getRoot();
    //if(StartUpPane.getChildren().size() >= 2)
    //{
    //    return;
    //}
    //FXMLLoader fxmlLoader = new FXMLLoader();
    //Parent root;
    // fxmlLoader.setLocation(getClass().getResource("FriendsListGUI.fxml"));
    //root = fxmlLoader.load();
    //FriendsListGUI friendsListGUI = fxmlLoader.getController();
    //friendsListGUI.setData(curr_conversation,root);
    //StartUpPane.getChildren().add(root);
    //}
    public void returnToChats(MouseEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("Chat.fxml")));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene=new Scene(root);
        //scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("CreateAccount.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
