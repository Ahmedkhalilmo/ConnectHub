package com.example.ConnectHub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationPanel {
    @FXML
    private Circle ProfileImageView;

    @FXML
    private Label UsernameTF;
    @FXML
    private Label DateLabel;
    VBox notificationBar;
    @FXML
    Parent NotificationBox;
    Notification curr_notification;

    public void setData(Notification notification, Parent root, VBox notificationBar) {
        curr_notification = notification;
        NotificationBox = root;
        this.notificationBar = notificationBar;
        UsernameTF.setText(notification.getMessage());
        LocalDateTime timestamp = notification.getTimestamp();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateLabel.setText(timestamp.format(formatter).toString());
        System.out.println(timestamp.format(formatter));
        Image image = new Image(getClass().getResourceAsStream(notification.getSender().getImageUrl()));
        ProfileImageView.setFill(new ImagePattern(image));
    }

    public void DeleteNotification(ActionEvent e) {
        notificationBar.getChildren().remove(NotificationBox);
        UserManager.removeNotification(UserManager.curr_user, curr_notification);
    }

    public void DeletePostNotification(MouseEvent e) {
        notificationBar.getChildren().remove(NotificationBox);
        UserManager.removeNotification(UserManager.curr_user, curr_notification);
    }

    public void acceptNotification(ActionEvent e) {
        FriendsManager friendsManager = new FriendsManager();
        friendsManager.acceptRequest(curr_notification.sender, UserManager.curr_user);
        notificationBar.getChildren().remove(NotificationBox);
        UserManager.removeNotification(UserManager.curr_user, curr_notification);
    }
}
