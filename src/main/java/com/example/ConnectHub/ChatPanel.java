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
    private Label ChatNameTF; // Ensure fx:id matches
    private Conversation curr_conversation;
    private Stage stage;
    private Scene scene;

    public void setData(Conversation conversation) {
        ChatNameTF.setText(conversation.getNameOfChat());
        curr_conversation = conversation;
    }

    public void openChat(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(this.getClass().getResource("Conversation.fxml")));
        Parent root = loader.load();
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        ConversationPanel conversationPanel = loader.getController();
        conversationPanel.setData(curr_conversation);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
