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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Home {
    private Stage stage;
    private Scene scene;
    private User user = UserManager.curr_user;  // Using the logged-in user from UserManager
    public static List<Post> posts = new ArrayList<>();
    private String imgUrl = user.getImageUrl();
    private static final String postFilePath = "posts.txt" ;
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
        loadPostsFromFile(); // Load saved posts from 'posts.txt'
        UsernameLabel.setText(user.getUsername());
        displayPosts();
        loadUserProfileImage();
        System.out.println("init");
//        Runtime.getRuntime().addShutdownHook(new Thread(this::savePostsToFile)); // Save posts on shutdown
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
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Chat.css")).toExternalForm());
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

            if (newPost.isLikedByCurrentUser()) {
                newPost.addLike(user);  // Ensure the user is added to the likers set
            }

            posts.add(newPost);
            postContentArea.clear();
            savePostsToFile(); // Save posts after creation
            displayPosts();
        } else {
            System.out.println("Cannot create an empty post.");
        }
    }

    public void uploadImage() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(stage); // Use the current stage
        if (selectedFile != null) {
            String relativePath = "src/main/resources/com/example/ConnectHub/PostPics";
            postImage = new Image(selectedFile.toURI().toString());
            Path from = Paths.get(selectedFile.toURI());
            String name = String.valueOf(posts.size());
            Path to = Paths.get(relativePath, name + ".png");
            System.out.println("saving path 1: " + to.toString());
            System.out.println("saving path 2: " + from.toString());
            try {
                if (!Files.exists(to)) {
                    Files.copy(from, to);
                } else {
                    System.out.println("File already exists: " + to);
                }
            } catch (IOException e) {
                System.err.println("Error copying file: " + e.getMessage());
            }

            System.out.println("Image uploaded: " + selectedFile.getName());
        }
    }

    public void displayPosts() {
        postsContainer.getChildren().clear();
        loadPostsFromFile();
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
            UserManager.sendNotification(user , post.getPoster(), 2);
        }
        savePostsToFile(); // Save posts after liking/unliking
    }


    private void savePostsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(postFilePath, false))) {
                for (Post post : posts) {
                    StringBuilder postBuilder = new StringBuilder();
                    postBuilder.append(post.id)
                            .append("->")
                            .append(post.getPoster().getUsername())
                            .append("/")
                            .append(post.getTextContent())
                            .append("/")
                            .append(post.getlikers().size())
                            .append("/")
                            .append(post.getImage()==null?"-":post.id)
                            .append(",");

                    for(User liker : post.getlikers()){
                        postBuilder.append(liker.getUsername())
                                .append("/");
                    }

                    String line = postBuilder.toString();
                    writer.write(line);
                    writer.newLine();
                }
            System.out.println("posts saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving posts: " + e.getMessage());
        }
    }

    private static void loadPostsFromFile() {
        posts.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(postFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("->");
                if (parts.length == 2) {
                    int id = Integer.parseInt(parts[0]);
                    String[] parts2 = parts[1].split(",");
                    String[] postData = parts2[0].split("/");

                    String[] likersData;
                    System.out.println("debug 1");
                    if(!postData[2].equals("0")) {
                        likersData = parts2[1].split("/");
                    }
                    else likersData = new String[0];

                    System.out.println("debug 2");
                    Post post = new Post(null, null);
                    System.out.println(postData[3]);
                    if(postData[3].equals("-")) {
                        post = new Post(postData[1], UserManager.getFriend(postData[0]));
                    }
                    else {
                        String path = "/com/example/ConnectHub/PostPics/" + id + ".png";
                        System.out.println("Reading resource: " + path);
                        Image img = new Image(Home.class.getResourceAsStream(path));
                        post = new Post(postData[1], img, UserManager.getFriend(postData[0]));
                    }
                    System.out.println("debug 3");
                    HashSet<User> likers = new HashSet<>();
                    for(String username : likersData){
                        likers.add(UserManager.getFriend(username));
                    }
                    post.setLikers(likers);
                    post.id = id;
                    posts.add(post);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("No files detected.");
        } catch (IOException e) {
            System.err.println("Error loading from file: " + e.getMessage());
        }
    }
}
