package com.example.ConnectHub;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class NotificationBar implements Initializable {
    @FXML
    VBox NotificationVBox;

    @FXML
    private HBox PostSelectedPanel;
    private Stage stage;
    private Scene scene;
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            for (Notification notification : UserManager.getUserNotifications(UserManager.curr_user)) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root;
                fxmlLoader.setLocation(getClass().getResource("NotificationPanel.fxml"));
                root = fxmlLoader.load();
                NotificationPanel notificationPanel = fxmlLoader.getController();
                notificationPanel.setData(notification,root,NotificationVBox);
                NotificationVBox.getChildren().add(root);
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public void returnToHomepage(MouseEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("Home.fxml")));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene=new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Home.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }


    public void openNotificationPanel(MouseEvent mouseEvent) {
    }
}
