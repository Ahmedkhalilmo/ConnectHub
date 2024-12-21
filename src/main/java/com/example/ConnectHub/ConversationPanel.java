package com.example.ConnectHub;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
        for(Message message: curr_conversation.getAllMessages())
        {
            createMsgPanel(message);
        }
    }

    private void createMsgPanel(Message message) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        BorderPane root;
        fxmlLoader.setLocation(getClass().getResource("Messages.fxml"));
        root = fxmlLoader.load();
        if(message.getUser().getUsername().toLowerCase().equals(UserManager.curr_user.getUsername().toLowerCase()))
        {
            HBox hBox = new HBox();
            root.setRight(hBox);
            root.setMargin(hBox, new Insets(25, 25, 25, 125)); // top, right, bottom, left
        }
        MessagePanel chatPanel = fxmlLoader.getController();
        chatPanel.setData(message);
        MessagesVBox.getChildren().add(root);
    }

    public void createNewMsg(MouseEvent e) throws IOException {
        Message message = new Message(UserManager.curr_user,MessageContentTF.getText());
        curr_conversation.sendMessage(UserManager.curr_user,message);
        createMsgPanel(message);
        MessageContentTF.clear();
        UserManager.saveChats();
    }

    public void addUser(MouseEvent e) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL fxmlLocation = getClass().getResource("FriendListGUI.fxml");
        fxmlLoader.setLocation(fxmlLocation);
        Parent root = fxmlLoader.load();
        FriendsListGUI friendsListGUI = fxmlLoader.getController();
        friendsListGUI.setData(curr_conversation, root);
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }


    public void returnToChats(MouseEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("Chat.fxml")));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
