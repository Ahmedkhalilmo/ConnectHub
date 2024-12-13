package com.example.ConnectHub;

import javafx.scene.image.Image;
import java.io.*;

class Post implements Serializable {
    private String textContent;
    private transient Image image; // Marked transient as Image is not serializable
    private String imageUrl; // Used to reload the image
    private User poster;  // Added field to store the user who created the post

    // Constructor for posts without an image
    public Post(String textContent, User poster) {
        this.textContent = textContent;
        this.image = null;
        this.imageUrl = null;
        this.poster = poster;  // Assign the user who created the post
    }

    // Constructor for posts with an image
    public Post(String textContent, Image image, User poster) {
        this.textContent = textContent;
        this.image = image;
        this.imageUrl = image != null ? image.getUrl() : null;
        this.poster = poster;  // Assign the user who created the post
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

    @Override
    public String toString() {
        return "Post{" + "textContent='" + textContent + '\'' + ", image=" + (image != null ? "Yes" : "No") + ", poster=" + poster.getUsername() + '}';
    }
}
