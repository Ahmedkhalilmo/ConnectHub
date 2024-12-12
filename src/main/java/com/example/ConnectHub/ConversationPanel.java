package com.example.ConnectHub;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.IOException;


public class ConversationPanel {
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
        fxmlLoader.setLocation(getClass().getResource("com/example/ConnectHub/Messages.fxml"));
        root = fxmlLoader.load();
        if (message.getUser().getUsername().toLowerCase().equals(UserManager.curr_user.getUsername().toLowerCase())) {
            HBox hBox = new HBox();
            root.setRight(hBox);
            root.setMargin(hBox, new Insets(25, 25, 25, 125)); // top, right, bottom, left
        }
        MessagePanel chatPanel = fxmlLoader.getController();
        chatPanel.setData(message);
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
        Pane rootPane = (Pane) ((Node) e.getSource()).getScene().getRoot();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Chat.fxml"));
        Parent root = fxmlLoader.load();
    }
}
