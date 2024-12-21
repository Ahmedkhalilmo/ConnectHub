package com.example.ConnectHub;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ChatPanel {
    @FXML
    private Label ChatNameTF;
    private Conversation curr_conversation;
    private Stage stage;
    private Scene scene;

    public void setData(Conversation conversation) {
        ChatNameTF.setText(conversation.getNameOfChat());
        curr_conversation = conversation;
    }

    public void openChat(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ConnectHub/Conversation.fxml"));
        Parent root = loader.load();
        ConversationPanel conversationPanel = loader.getController();
        conversationPanel.setData(curr_conversation);
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Chat.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
