package com.example.ConnectHub;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class NotificationPanel {
    @FXML
    private Circle ProfileImageView;

    @FXML
    private Label UsernameTF;
    VBox notificationBar;
    @FXML
    Parent NotificationBox;
    Notification curr_notification;
    public void setData(Notification notification,Parent root,VBox notificationBar)
    {
        curr_notification = notification;
        NotificationBox = root;
        this.notificationBar = notificationBar;
        UsernameTF.setText(notification.getMessage());
        Image image = new Image(getClass().getResourceAsStream(notification.getSender().getImageUrl()));
        ProfileImageView.setFill(new ImagePattern(image));
    }
    public void DeleteNotification(MouseEvent e)
    {
        notificationBar.getChildren().remove(NotificationBox);
        UserManager.removeNotification(UserManager.curr_user,curr_notification);
    }
}
