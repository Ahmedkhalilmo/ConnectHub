package com.example.ConnectHub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class ProfilePage extends Profile
{
    public static boolean isDarkTheme = false;

    public static User friendUser;

    @FXML
    public VBox ProfilePostsContainer;

    public ProfilePage() {
        super();
    }

    public void initialize()
    {
        loadprofileposts(myUser);
        displayPosts(ProfilePostsContainer);
        nameLabel.setText(myUser.getUsername());
        Image image = new Image(getClass().getResourceAsStream(myUser.getImageUrl()));
        CircleImageView.setFill(new ImagePattern(image));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDateString = myUser.getUserBirthDate().format(formatter);

        BirthDateL.setText(formattedDateString);
        GenderL.setText(myUser.getGender());
        EmailL.setText(myUser.getEmail());

        List<String> friends = FriendsManager.getUserFriends(myUser.getUsername());
        friends.removeIf(friend -> friend.equals(myUser.getUsername()));
        friendsListView.getItems().addAll(friends);
        friendsListView.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        friendsListView.setCellFactory(param -> new ListCell<String>()
        {
            private final HBox hbox = new HBox();
            private final ImageView imageView = new ImageView();
            private final Label label = new Label();

            {
                hbox.setSpacing(10);
                imageView.setFitHeight(40);
                imageView.setFitWidth(40);
                hbox.getChildren().addAll(imageView, label);
            }

            @Override
            protected void updateItem(String username, boolean empty)
            {
                super.updateItem(username, empty);

                if (empty || username == null)
                {
                    setGraphic(null);
                }
                else
                {
                    User friend = UserManager.getFriend(username); // Assuming this retrieves friend info
                    Image image = new Image(getClass().getResourceAsStream(friend.getImageUrl()));
                    imageView.setImage(image);
                    label.setText(username);
                    setGraphic(hbox);
                }
            }
        });

        friendsListView.setOnMouseClicked(this::onUserClick);
    }


    private void onUserClick(MouseEvent event)
    {
        String selectedUsername = friendsListView.getSelectionModel().getSelectedItem();
        if (selectedUsername != null)
        {
            System.out.println("Selected user: " + selectedUsername);
            friendUser = UserManager.getFriend(selectedUsername);
            switchToFriendProfile();
        }
    }

    protected void switchToFriendProfile()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Friend.fxml"));            Parent root = loader.load();
            stage = (Stage) friendsListView.getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("profile.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void returntoHomepage(MouseEvent e) throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("Home.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(isDarkTheme?"DarkHome.css":"Home.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void LogOut(javafx.event.ActionEvent actionEvent) throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("login.fxml")));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(isDarkTheme?"DarkLogin.css":"Login.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double centerX = (screenBounds.getWidth() - stage.getWidth()) / 2;
        double centerY = (screenBounds.getHeight() - stage.getHeight()) / 2;
        stage.setX(centerX);
        stage.setY(centerY);
    }

    public void toggleThemeAndRefresh(ActionEvent actionEvent) {
       isDarkTheme = !isDarkTheme;
       System.out.println("Theme toggled: " + (isDarkTheme ? "Dark" : "Light"));
        refreshCurrentPage();
    }
    private void refreshCurrentPage() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("ProfilePage.fxml")));
            stage = (Stage) ProfilePostsContainer.getScene().getWindow();
            scene = new Scene(root);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(ProfilePage.isDarkTheme?"Darkprofile.css":"profile.css")).toExternalForm());


            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
