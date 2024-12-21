package com.example.ConnectHub;

import javafx.scene.image.Image;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class Post implements Serializable {
    private String textContent;
    private transient Image image; // Marked transient as Image is not serializable
    private String imageUrl; // Used to reload the image
    private User poster;  // Added field to store the user who created the post
    private int likeCount; // Add like count
    private Set<User> likers;
    private boolean likedByCurrentUser; // To track the "liked" status for the current user
    public static int counter =0;
    public int id;

    // Constructor for posts without an image
    public Post(String textContent, User poster) {
        this.textContent = textContent;
        this.image = null;
        this.imageUrl = null;
        this.poster = poster;  // Assign the user who created the post
        this.likeCount = 0;  // Initialize like count
        this.likers = new HashSet<>();
        this.likedByCurrentUser = false; // Default to false
        this.id = Home.posts.size();
    }

    // Constructor for posts with an image
    public Post(String textContent, Image image, User poster) {
        this.textContent = textContent;
        this.image = image;
        this.imageUrl = image != null ? image.getUrl() : null;
        this.poster = poster;  // Assign the user who created the post
        this.likeCount = 0;  // Initialize like count
        this.likers = new HashSet<>();
        this.likedByCurrentUser = false; // Default to false
        this.id = Home.posts.size();
    }

    // Set the like status for the current user
    public void setLikedByCurrentUser(boolean liked) {
        this.likedByCurrentUser = liked;
    }
    public void setLikers(HashSet<User> liker){
        this.likeCount = liker.size();
        likers = liker;
    }
    public void addLike(User user) {
        this.likers.add(user);  // Add the user to the likers set
        setLikedByCurrentUser(true); // Set as liked by current user
    }

    public void removeLike(User user) {
        this.likers.remove(user);  // Remove the user from the likers set
        setLikedByCurrentUser(false); // Set as not liked by current user
    }

    public boolean isLikedByCurrentUser() {
        return getlikers().contains(UserManager.curr_user);
    }

    public Set<User> getlikers() {
        return this.likers;  // Return the set of users who liked the post
    }

    public String getTextContent() {
        return textContent;
    }

    public Image getImage() {
        if (image == null && imageUrl != null) {
            image = new Image(imageUrl); // Reload the image from the URL
        }
        return image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public User getPoster() {
        return poster;  // Return the user who created the post
    }


    // Override to properly save and load the image URL
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject(); // Write default fields
        oos.writeObject(imageUrl); // Save the image URL instead of the Image object
    }

    // Override to reload the image URL when loading the post
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject(); // Read default fields
        imageUrl = (String) ois.readObject(); // Read the image URL
        if (imageUrl != null) {
            image = new Image(imageUrl); // Reload the image using the URL
        }
    }

    @Override
    public String toString() {
        return "Post{" + "textContent='" + textContent + '\'' + ", image=" + (image != null ? "Yes" : "No") + ", poster=" + poster.getUsername() + ", likes=" + likeCount + '}';
    }
}
