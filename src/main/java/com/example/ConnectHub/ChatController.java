package com.example.ConnectHub;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    private VBox ChatsVBox;
    @FXML
    TextField ChatNameTF;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (Conversation conversation : UserManager.chats) {
            if (!conversation.getUsernamesOfParticipants().contains(UserManager.curr_user.getUsername().toLowerCase())) {
                continue;
            }
            try {
                ConversationPanelCreate(conversation);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addChat(MouseEvent e) throws IOException {

        if (ChatNameTF.getText().isEmpty()) {
            return;
        }
        Conversation newconversation = new Conversation(UserManager.curr_user, ChatNameTF.getText());
        UserManager.addConversation(newconversation);
        ConversationPanelCreate(newconversation);
    }

    private void ConversationPanelCreate(Conversation conversation) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root;
        fxmlLoader.setLocation(getClass().getResource("UserOfChat.fxml"));
        root = fxmlLoader.load();
        ChatPanel chatPanel = fxmlLoader.getController();
        chatPanel.setData(conversation);
        ChatsVBox.getChildren().add(root);
    }

    public void returntoHomepage(MouseEvent e) throws IOException {
        StackPane StartUpPane = (StackPane) ((Node) e.getSource()).getScene().getRoot();
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        SceneTransitions.doFadeIn(StartUpPane, root);
    }
}
