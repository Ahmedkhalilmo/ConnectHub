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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Home {
    private Stage stage;
    private Scene scene;
    private User user = UserManager.curr_user;
    private List<Post> posts = new ArrayList<>();

    @FXML
    private TextField postContentArea;

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
    }

    public void OpenChat(javafx.scene.input.MouseEvent e) throws IOException {
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



    public void createPost() {
        if (PostCardLayout != null) {
            String content = postContentArea.getText();

            if (!content.trim().isEmpty()) {
                Image currentPostImage = postImage;
                postImage = null;


                Post newPost = new Post(content, currentPostImage);
                posts.add(newPost);


                postContentArea.clear();


                displayPosts();
            } else {
                System.out.println("Cannot create an empty post.");
            }
        } else {
            System.out.println("PostCardLayout is null");
        }
    }



    // Handle image upload
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


    // Display all posts in the postsContainer
    public void displayPosts() {
        postsContainer.getChildren().clear();


        for (Post post : posts) {
            VBox postBox = new VBox();
            postBox.setSpacing(10);
            postBox.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-padding: 10px; -fx-border-color: #d3d3d3; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, #000000, 10, 0.2, 0, 0);");


            HBox postHeader = new HBox();
            postHeader.setSpacing(10);
            postHeader.setStyle("-fx-alignment: center-left;");


            Circle profileImage = new Circle(20);
            profileImage.setFill(Color.DODGERBLUE);
            postHeader.getChildren().add(profileImage);

            // User name
            Label userNameLabel = new Label(UsernameLabel.getText());
            userNameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            postHeader.getChildren().add(userNameLabel);

            // Add the post header (user info) to the post box
            postBox.getChildren().add(postHeader);

            // Post content (text)
            TextArea textArea = new TextArea(post.getTextContent());
            textArea.setEditable(false);  // Make sure the text isn't editable
            textArea.setWrapText(true);
            textArea.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-font-size: 14px;");

            // Add text content to the post box
            postBox.getChildren().add(textArea);

            // Image (only if it's not null)
            if (post.getImage() != null) {
                ImageView imageView = new ImageView(post.getImage());
                imageView.setFitWidth(300);  // Resize image to fit
                imageView.setPreserveRatio(true);
                postBox.getChildren().add(imageView);  // Add the image under the text
            }

            // Add the complete post card to the postsContainer
            postsContainer.getChildren().add(postBox);
        }
    }


    // Delete a post from the list
}



class Post {
    private String textContent;
    private Image image;

    // Constructor to allow posts without an image
    public Post(String textContent) {
        this.textContent = textContent;
        this.image = null;
    }

    // Constructor to allow posts with an image
    public Post(String textContent, Image image) {
        this.textContent = textContent;
        this.image = image;
    }

    public String getTextContent() {
        return textContent;
    }

    public Image getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "Post{" +
                "textContent='" + textContent + '\'' +
                ", image=" + (image != null ? "Yes" : "No") +
                '}';
    }
}
