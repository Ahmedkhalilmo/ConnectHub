package com.example.ConnectHub;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.util.Objects;

public class ChatPanel {
    @FXML
    Label ChatNameTF;
    Conversation curr_conversation;
    private Stage stage;
    private Scene scene;
    public void setData(Conversation conversation)
    {
        ChatNameTF.setText(conversation.getNameOfChat());
        curr_conversation = conversation;
    }
    public void openChat(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("Conversation.fxml")));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        ConversationPanel conversationPanel = loader.getController();
        conversationPanel.setData(curr_conversation);
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
