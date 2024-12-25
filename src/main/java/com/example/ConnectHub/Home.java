package com.example.ConnectHub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Home {
    private Stage stage;
    private Scene scene;
    private User user = UserManager.curr_user;  // Using the logged-in user from UserManager
    public static List<Post> posts = new ArrayList<>();
    private String imgUrl = user.getImageUrl();
    private static final String postFilePath = "posts.txt";
    @FXML
    private TextField postContentArea;
    @FXML
    private RadioButton isPrivateButton;
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

    String textColor = ProfilePage.isDarkTheme ? "white" : "#2a2a2a";
    String BGColor = ProfilePage.isDarkTheme ? "#2a2a2a" : "white";
    public void initialize() {
//        loadPostsFromFile(); // Load saved posts from 'posts.txt'
        UsernameLabel.setText(user.getUsername());
        displayPosts();
        loadUserProfileImage();
        System.out.println("init");
//        Runtime.getRuntime().addShutdownHook(new Thread(this::savePostsToFile)); // Save posts on shutdown
    }

    public void toggleThemeAndRefresh(MouseEvent e) {
        ProfilePage.isDarkTheme = !ProfilePage.isDarkTheme;
        System.out.println("Theme toggled: " + (ProfilePage.isDarkTheme ? "Dark" : "Light"));
        refreshCurrentPage();
    }

    private void refreshCurrentPage() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("Home.fxml")));
            stage = (Stage) postsContainer.getScene().getWindow();
            scene = new Scene(root);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(ProfilePage.isDarkTheme?"DarkHome.css":"Home.css")).toExternalForm());


            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(ProfilePage.isDarkTheme ? "DarkChat.css" :"Chat.css")).toExternalForm());
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
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(ProfilePage.isDarkTheme?"Darkprofile.css":"profile.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void createPost() {
        String content = postContentArea.getText();
        boolean isPrivate = isPrivateButton.isSelected();
        if (!content.trim().isEmpty()) {
            Image currentPostImage = postImage;
            postImage = null;
            Post newPost = new Post(content, currentPostImage, user, isPrivate);

            if (newPost.isLikedByCurrentUser()) {
                newPost.addLike(user);  // Ensure the user is added to the likers set
            }

            posts.add(newPost);
            postContentArea.clear();
//            savePostsToFile(); // Save posts after creation
            displayPosts();
            System.out.println("*****======================");
            String tag = extractTag(content);
            User taggeduser = UserManager.getFriend(tag);
            System.out.println(content);
            System.out.println(tag);
            System.out.println(taggeduser);
            UserManager.sendNotification(user, taggeduser,4);
            System.out.println("======================");
        } else {
            System.out.println("Cannot create an empty post.");
        }
    }

    public static String extractTag(String input) {
        if (input == null || input.isEmpty()) {
            return null; // Return null if the input is empty
        }

        int atIndex = input.indexOf('@');
        if (atIndex == -1) {
            return null; // Return null if no '@' is found
        }

        // Find the end of the tag
        int endIndex = input.indexOf(' ', atIndex);
        if (endIndex == -1) {
            endIndex = input.length(); // If no space, take the rest of the string
        }

        return input.substring(atIndex + 1, endIndex); // Extract the tag
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
        postsContainer.setTranslateX(50);
//        loadPostsFromFile();
//        CommentsManager.loadCommentsFromFile();
        for (Post post : posts) {
            System.out.println(post.getIsPrivate());
            System.out.println((FriendsManager.getUserFriends(post.getPoster().getUsername()).contains(UserManager.curr_user.getUsername())));
            System.out.println(!(post.getPoster().getUsername().equals(UserManager.curr_user.getUsername())));
            if(post.getIsPrivate() &&
                    !(FriendsManager.getUserFriends(post.getPoster().getUsername()).contains(UserManager.curr_user.getUsername()))
                    && !(post.getPoster().getUsername().equals(UserManager.curr_user.getUsername()))) continue;
            VBox postBox = createPostBox(post);

            // Create like button and set its state depending on whether the current user liked the post
            Button likeButton = createLikeButton(post);
            postBox.getChildren().add(likeButton); // Add the like button

            VBox commentsBox = new VBox();
            commentsBox.setSpacing(10);
            commentsBox.setStyle("-fx-background-color:" + BGColor + "; -fx-padding: 10px; -fx-border-width: 1px; -fx-border-radius: 5px;");
            commentsBox.setMaxHeight(100);

            TextField commentInput = new TextField();
            commentInput.setPromptText("Write a comment...");
            commentInput.setPrefWidth(280);
            commentInput.setStyle("-fx-text-fill: "+textColor+";-fx-font-size: 12px; -fx-padding: 5px;");

            Button addCommentButton = new Button("Add Comment");
            addCommentButton.setStyle("-fx-font-size: 12px; -fx-background-color: #4CAF50; -fx-text-fill: white;"
                    + "-fx-padding: 5px; -fx-border-radius: 5px;");
            refreshCommentsDisplay(commentsBox, post);
            addCommentButton.setOnAction(e -> {
                String newCommentText = commentInput.getText();
                if (newCommentText != null && !newCommentText.trim().isEmpty()) {
                    CommentsManager.addComment(post.id, new Comment(newCommentText, UserManager.curr_user));
                    commentInput.clear();
                    refreshCommentsDisplay(commentsBox, post);
                }
            });
            HBox addCommentBox = new HBox(10, commentInput, addCommentButton);
            addCommentBox.setAlignment(Pos.CENTER_LEFT);
            addCommentBox.setStyle("-fx-padding: 5px;");

            postBox.getChildren().addAll(commentsBox, addCommentBox);
            if (!postBox.getChildren().contains(commentsBox)) {
                postBox.getChildren().add(commentsBox);
            }
            postsContainer.getChildren().add(postBox);
        }
    }


    private void refreshCommentsDisplay(VBox commentsBox, Post post) {
        commentsBox.getChildren().clear();
        ArrayList<Comment> comments = CommentsManager.getComments(post.id);
        if(comments == null || comments.size() ==0) return;
        for (Comment comment : comments) {
            String commentText = comment.getCommenter().getUsername() + " : " + comment.getCommentText()
                    + "  | at " + comment.time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            Label commentLabel = new Label(commentText);
            commentLabel.setMaxWidth(300);
            commentLabel.setWrapText(true);
            commentLabel.setStyle("-fx-font-size: 12px; -fx-background-color: "+BGColor+"; -fx-padding: 5px; "
                    + " -fx-border-width: 1px; -fx-border-radius: 5px;");
            commentsBox.getChildren().add(commentLabel);
        }
    }


    private VBox createPostBox(Post post) {
        VBox postBox = new VBox();
        postBox.setSpacing(15);
     //   postBox.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-padding: 10px; -fx-border-color: #d3d3d3; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, #000000, 10, 0.2, 0, 0);");

        HBox postHeader = createPostHeader(post);
        postBox.getChildren().add(postHeader);

        TextArea textArea = new TextArea(post.getTextContent());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-background-color: "+BGColor + "; -fx-border-color: transparent; -fx-font-size: 14px;");
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
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(ProfilePage.isDarkTheme ?"DarkSearch.css":"SearchResults.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void openNotificationPanel(MouseEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("NotificationBar.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(ProfilePage.isDarkTheme ?"DarkNotificationBar.css":"NotificationBar.css")).toExternalForm());

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
            if (user.getUsername() != post.getPoster().getUsername())
                UserManager.sendNotification(user, post.getPoster(), 2);
        }
//        savePostsToFile();
    }


    static void savePostsToFile() {

        ArrayList<String> lines = new ArrayList<>();
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
                    .append(post.getIsPrivate())
                    .append("/")
                    .append(post.getImage() == null ? "-" : post.id)
                    .append(",");

            for (User liker : post.getlikers()) {
                postBuilder.append(liker.getUsername())
                        .append("/");
            }

            String line = postBuilder.toString();
            lines.add(line);
        }
        FilesRW.writeToFile(postFilePath, lines);
    }

    public static void loadPostsFromFile() {
        posts.clear();
        ArrayList<String> data = FilesRW.readFromFile(postFilePath);
        if (data == null) {
            posts = new ArrayList<>();
            return;
        }
        for (String line : data) {
            String[] parts = line.split("->");
            if (parts.length == 2) {
                int id = Integer.parseInt(parts[0]);
                String[] parts2 = parts[1].split(",");
                String[] postData = parts2[0].split("/");

                String[] likersData;
                if (!postData[2].equals("0")) {
                    likersData = parts2[1].split("/");
                } else likersData = new String[0];

                Post post = new Post(null, null, false);
                if (postData[4].equals("-")) {
                    post = new Post(postData[1], UserManager.getFriend(postData[0]), postData[3].equals("true") );
                } else {
                    String path = "src/main/resources/com/example/ConnectHub/PostPics/" + id + ".png";
                    File imageFile = new File(path);
                    if (imageFile.exists()) {
                        Image img = new Image(imageFile.toURI().toString());
                        post = new Post(postData[1], img, UserManager.getFriend(postData[0]), postData[3].equals("true"));
                    } else {
                        System.err.println("Image file not found: " + path);
                        post = new Post(postData[1], UserManager.getFriend(postData[0]), postData[3].equals("true"));
                    }
                }

                HashSet<User> likers = new HashSet<>();
                for (String username : likersData) {
                    likers.add(UserManager.getFriend(username));
                }
                post.setLikers(likers);
                post.id = id;
                posts.add(post);
            }
        }
    }
}
