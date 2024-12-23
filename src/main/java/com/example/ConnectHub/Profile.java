package com.example.ConnectHub;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class Profile
{
    protected Stage stage;

    protected Scene scene;

    protected User myUser = UserManager.curr_user;

    public static List<Post> profileposts = new ArrayList<>();

    private static final String postFilePath = "posts.txt" ;
    @FXML
    protected Label nameLabel;
    @FXML
    protected Circle CircleImageView;
    @FXML
    protected Label BirthDateL;

    @FXML
    protected Label GenderL;

    @FXML
    protected Label EmailL;

    @FXML
    protected ListView<String> friendsListView;
    @FXML
    protected ListView<String> MutualfriendsListView;

    public void initialize(){
        loadprofileposts(myUser);
    }

    public void loadprofileposts(User user) {
        profileposts.clear();
        for (Post post : Home.posts) {
            if(post.getIsPrivate() &&
                    !(FriendsManager.getUserFriends(post.getPoster().getUsername()).contains(UserManager.curr_user.getUsername()))
                    && !(post.getPoster().getUsername().equals(UserManager.curr_user.getUsername()))) continue;
            if(post.getPoster().getUsername().equals(user.getUsername()))
                profileposts.add(post);
        }
//CommentsManager.loadCommentsFromFile();
    }

    private VBox createPostBox(Post post)
    {
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

        VBox commentsBox = new VBox();
        commentsBox.setSpacing(10);
        commentsBox.setStyle("-fx-background-color: #f9f9f9; -fx-padding: 10px; -fx-border-color: #d3d3d3; -fx-border-width: 1px; -fx-border-radius: 5px;");
        commentsBox.setMaxHeight(100);
//        refreshCommentsDisplay(commentsBox, post);
//        List<Comment> comments = CommentsManager.getComments(post.id);

        // Create a text box for adding new comments
        TextField commentInput = new TextField();
        commentInput.setPromptText("Write a comment...");
        commentInput.setPrefWidth(280);
        commentInput.setStyle("-fx-font-size: 12px; -fx-padding: 5px;");

// Create a button to submit the comment
        Button addCommentButton = new Button("Add Comment");
        addCommentButton.setStyle("-fx-font-size: 12px; -fx-background-color: #4CAF50; -fx-text-fill: white; "
                + "-fx-padding: 5px; -fx-border-radius: 5px;");
        refreshCommentsDisplay(commentsBox, post);
        addCommentButton.setOnAction(e -> {
            String newCommentText = commentInput.getText();
            if (newCommentText != null && !newCommentText.trim().isEmpty()) {
                // Add the new comment to the comments list
                CommentsManager.addComment(post.id, new Comment(newCommentText, myUser));
                // Clear the input field
                commentInput.clear();
                refreshCommentsDisplay(commentsBox, post);
                // Refresh the comments display
            }
        });

// Add the input field and button to a container
        HBox addCommentBox = new HBox(10, commentInput, addCommentButton);
        addCommentBox.setAlignment(Pos.CENTER_LEFT);
        addCommentBox.setStyle("-fx-padding: 5px;");

// Add the comments box and input area to the post box
        postBox.getChildren().addAll(commentsBox, addCommentBox);
// Ensure the commentsBox is added only once to postBox
        if (!postBox.getChildren().contains(commentsBox)) {
            postBox.getChildren().add(commentsBox);
        }



        return postBox;
    }

    private void refreshCommentsDisplay(VBox commentsBox, Post post) {
//        CommentsManager.loadCommentsFromFile();
        commentsBox.getChildren().clear(); // Clear existing comments
        ArrayList<Comment> comments = CommentsManager.getComments(post.id);
        if(comments == null || comments.size() ==0) return;
        for (Comment comment : comments) {
            String commentText = comment.getCommenter().getUsername() + " : " + comment.getCommentText()
                    + "  | at " + comment.time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            Label commentLabel = new Label(commentText);
            commentLabel.setMaxWidth(300);
            commentLabel.setWrapText(true);
            commentLabel.setStyle("-fx-font-size: 12px; -fx-background-color: white; -fx-padding: 5px; "
                    + "-fx-border-color: #d3d3d3; -fx-border-width: 1px; -fx-border-radius: 5px;");
            commentsBox.getChildren().add(commentLabel); // Add each comment to the VBox
        }
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
    private void likePost(Post post, Button likeButton) {
        if (post.isLikedByCurrentUser()) {
            post.removeLike(myUser); // Remove current user from the likers set
            likeButton.setText("Like");
            likeButton.setStyle("-fx-background-color: #4CAF50; -fx-padding: 10px 10px; -fx-font-size: 14px;");
        } else {
            post.addLike(myUser); // Add current user to the likers set
            likeButton.setText("Liked");
            likeButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-border-radius: 5px;");
            if(myUser.getUsername() != post.getPoster().getUsername())
                UserManager.sendNotification(myUser , post.getPoster(), 2);
        }
//        Home.savePostsToFile();
    }
    public void displayPosts(VBox ProfilePostsContainer) {
        ProfilePostsContainer.getChildren().clear();
        for (Post post : profileposts) {
            VBox postBox = createPostBox(post);
            // Create like button and set its state depending on whether the current user liked the post
            Button likeButton = createLikeButton(post);
            postBox.getChildren().add(likeButton); // Add the like button
            ProfilePostsContainer.getChildren().add(postBox);
        }
    }

    protected  void switchToFriendProfile() {};

    protected abstract void returntoHomepage(MouseEvent e) throws IOException ;

    protected void LogOut(javafx.event.ActionEvent actionEvent) throws IOException {};
}
