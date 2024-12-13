package com.example.ConnectHub;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    private Stage stage;
    private Scene scene;
    @FXML
    private VBox ChatsVBox;
    @FXML
    TextField ChatNameTF;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserManager.loadChats(); // Load chats when the controller is initialized
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

        if(ChatNameTF.getText().isEmpty())
        {
            return;
        }
        Conversation newconversation = new Conversation(UserManager.curr_user,ChatNameTF.getText());
        UserManager.addConversation(newconversation);
        ConversationPanelCreate(newconversation);
        UserManager.saveChats();
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
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("Home.fxml")));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene=new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Home.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
