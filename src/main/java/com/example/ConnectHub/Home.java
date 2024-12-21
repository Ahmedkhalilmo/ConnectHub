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
        loadUserProfileImage();
        loadPostsFromFile(); // Load saved posts from 'posts.dat'
        Runtime.getRuntime().addShutdownHook(new Thread(this::savePostsToFile)); // Save posts on shutdown
    }

    private void loadUserProfileImage() {
        try {
            Image image = new Image(getClass().getResourceAsStream(user.getImageUrl()));
            ProfileImageView.setFill(new ImagePattern(image));
        } catch (Exception e) {
            ProfileImageView.setFill(new ImagePattern(new Image("file:default-profile-image.png")));
        }
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
        String content = postContentArea.getText();

        if (!content.trim().isEmpty()) {
            Image currentPostImage = postImage;
            postImage = null;
            Post newPost = new Post(content, currentPostImage, user);

            // Add the current user to the likers set if they liked the post
            if (newPost.isLikedByCurrentUser()) {
                newPost.addLike(user);  // Ensure the user is added to the likers set
            }

            posts.add(newPost);
            postContentArea.clear();
            displayPosts();
            savePostsToFile(); // Save posts after creation
        } else {
            System.out.println("Cannot create an empty post.");
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
            VBox postBox = createPostBox(post);

            // Create like button and set its state depending on whether the current user liked the post
            Button likeButton = createLikeButton(post);
            postBox.getChildren().add(likeButton); // Add the like button

            postsContainer.getChildren().add(postBox);
        }
    }


    private VBox createPostBox(Post post) {
        VBox postBox = new VBox();
        postBox.setSpacing(15);
        postBox.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-padding: 10px; -fx-border-color: #d3d3d3; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, #000000, 10, 0.2, 0, 0);");

        HBox postHeader = createPostHeader(post);
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

        return postBox;
    }

    private HBox createPostHeader(Post post) {
        HBox postHeader = new HBox();
        postHeader.setSpacing(10);
        postHeader.setStyle("-fx-alignment: center-left;");

        ImageView profileImage = new ImageView();
        profileImage.setFitWidth(40);
        profileImage.setFitHeight(40);
        profileImage.setPreserveRatio(true);

        Circle profileImageMask = new Circle(20);
        profileImageMask.setCenterX(20);
        profileImageMask.setCenterY(20);

        if (post.getPoster() != null && post.getPoster().getImageUrl() != null) {
            try {
                InputStream imageStream = getClass().getResourceAsStream(post.getPoster().getImageUrl());
                if (imageStream != null) {
                    profileImage.setImage(new Image(imageStream));
                } else {
                    profileImage.setImage(new Image("file:default-profile-image.png"));
                }
            } catch (Exception e) {
                profileImage.setImage(new Image("file:default-profile-image.png"));
            }
        } else {
            profileImage.setImage(new Image("file:default-profile-image.png"));
        }

        profileImage.setClip(profileImageMask);
        postHeader.getChildren().add(profileImage);

        Label userNameLabel = new Label(post.getPoster().getUsername());
        userNameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        postHeader.getChildren().add(userNameLabel);

        return postHeader;
    }


    private Button createLikeButton(Post post) {
        Button likeButton = new Button("Like");
        likeButton.setStyle("-fx-background-color: #4CAF50; -fx-padding: 10px 10px; -fx-font-size: 14px;");
        likeButton.setOnAction(event -> likePost(post, likeButton));

        // Set the initial button state depending on whether the current user liked the post
        if (post.isLikedByCurrentUser()) {
            likeButton.setText("Liked");
            likeButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-border-radius: 5px;");
        }

        return likeButton;
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

    public void openNotificationPanel(MouseEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("NotificationBar.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("NotificationBar.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();
    }
    private void likePost(Post post, Button likeButton) {
        if (post.isLikedByCurrentUser()) {
            post.removeLike(user); // Remove current user from the likers set
            likeButton.setText("Like");
            likeButton.setStyle("-fx-background-color: #4CAF50; -fx-padding: 10px 10px; -fx-font-size: 14px;");
        } else {
            post.addLike(user); // Add current user to the likers set
            likeButton.setText("Liked");
            likeButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-border-radius: 5px;");
        }

        savePostsToFile(); // Save posts after liking/unliking
    }


    private void savePostsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("posts.dat"))) {
            oos.writeObject(posts);  // Save the posts list to the 'posts.dat' file
        } catch (IOException e) {
            System.err.println("Error saving posts to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadPostsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("posts.dat"))) {
            posts = (List<Post>) ois.readObject();  // Load the list of posts
            displayPosts();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading posts from file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
