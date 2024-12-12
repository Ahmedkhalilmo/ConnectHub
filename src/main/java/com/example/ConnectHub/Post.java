package com.example.ConnectHub;


import javafx.scene.image.Image;

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
        return "Post{" + "textContent='" + textContent + '\'' + ", image=" + (image != null ? "Yes" : "No") + '}';
    }
}
