package com.example.ConnectHub;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Home {
    private Stage stage;
    private Scene scene;
    private User user = UserManager.curr_user;  // Using the logged-in user from UserManager
    private List<Post> posts = new ArrayList<>();
    private String imgUrl = user.getImageUrl();

    @FXML
    private TextField postContentArea;
    @FXML
    private TextField searchBar;
    @FXML
    private VBox postsContainer;
    @FXML
    private VBox PostCardLayout;

    private Image postImage;
    @FXML
    private Label UsernameLabel;

    @FXML
    private Circle ProfileImageView;

    public void initialize() {
        UsernameLabel.setText(user.getUsername());
        Image image = new Image(getClass().getResourceAsStream(user.getImageUrl()));
        ProfileImageView.setFill(new ImagePattern(image));
        System.out.println( user.getPassword());
        loadPostsFromFile(); // Load saved posts

        Runtime.getRuntime().addShutdownHook(new Thread(this::savePostsToFile)); // Save posts on shutdown
    }

    public void OpenChat(MouseEvent e) throws IOException {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("Chat.fxml")));
            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.err.println("Error loading Chat.fxml: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void OpenProfile(MouseEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("ProfilePage.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("profile.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void createPost() {
        if (PostCardLayout != null) {
            String content = postContentArea.getText();

            if (!content.trim().isEmpty()) {
                Image currentPostImage = postImage;
                postImage = null;
                // Now passing the current user who is creating the post
                Post newPost = new Post(content, currentPostImage, user);
                posts.add(newPost);
                postContentArea.clear();
                displayPosts();
                savePostsToFile(); // Save posts after creation
            } else {
                System.out.println("Cannot create an empty post.");
            }
        } else {
            System.out.println("PostCardLayout is null");
        }
    }

    public void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(stage); // Use the current stage
        if (selectedFile != null) {
            postImage = new Image(selectedFile.toURI().toString());
            System.out.println("Image uploaded: " + selectedFile.getName());
        }
    }

    public void displayPosts() {
        postsContainer.getChildren().clear();

        for (Post post : posts) {
            VBox postBox = new VBox();
            postBox.setSpacing(15);
            postBox.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-padding: 10px; -fx-border-color: #d3d3d3; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, #000000, 10, 0.2, 0, 0);");

            HBox postHeader = new HBox();
            postHeader.setSpacing(10);
            postHeader.setStyle("-fx-alignment: center-left;");

            ImageView profileImage = new ImageView();
            profileImage.setFitWidth(40);
            profileImage.setFitHeight(40);
            profileImage.setPreserveRatio(false);

            Circle profileImageMask = new Circle(20);
            profileImageMask.setCenterX(20);
            profileImageMask.setCenterY(20);

            // Set the image inside the circular mask
            if (post.getPoster() != null && post.getPoster().getImageUrl() != null) {
                try {
                    InputStream imageStream = getClass().getResourceAsStream(post.getPoster().getImageUrl());
                    if (imageStream != null) {
                        profileImage.setImage(new Image(imageStream));
                    } else {
                        System.err.println("Image not found: " + post.getPoster().getImageUrl());
                        profileImage.setImage(new Image("file:default-profile-image.png"));  // Fallback default image
                    }
                } catch (Exception e) {
                    System.err.println("Error loading profile image: " + e.getMessage());
                    profileImage.setImage(new Image("file:default-profile-image.png"));  // Fallback default image
                }
            } else {
                profileImage.setImage(new Image("file:default-profile-image.png"));
            }

            profileImage.setClip(profileImageMask);

            postHeader.getChildren().add(profileImage);

            Label userNameLabel = new Label(post.getPoster().getUsername()); // Display the username of the post creator
            userNameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            postHeader.getChildren().add(userNameLabel);

            postBox.getChildren().add(postHeader);

            TextArea textArea = new TextArea(post.getTextContent());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-font-size: 14px;");

            postBox.getChildren().add(textArea);

            if (post.getImage() != null) {
                ImageView imageView = new ImageView(post.getImage());
                imageView.setFitWidth(300);
                imageView.setPreserveRatio(true);
                postBox.getChildren().add(imageView);
            }

            postsContainer.getChildren().add(postBox);
        }
    }

    public void openNotificationPanel(MouseEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("NotificationBar.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("NotificationBar.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void savePostsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("posts.dat"))) {
            oos.writeObject(posts);
            System.out.println("Posts saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving posts: " + e.getMessage());
        }
    }

    public void loadPostsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("posts.dat"))) {
            posts = (List<Post>) ois.readObject();
            displayPosts(); // Refresh the UI with loaded posts
            System.out.println("Posts loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading posts: " + e.getMessage());
        }
    }

    public void switchtoresult(javafx.event.ActionEvent e) throws IOException {
        String search = searchBar.getText();
        UserManager.getUsers(search);
        if (SearchResults.usersearch.isEmpty()) {
            SearchResults.isEmpty = true;
        }

        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("SearchResults.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("SearchResults.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
