package com.example.ConnectHub;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


class Comment {
    private String commentText;
    private User commenter;
    LocalDateTime time;

    public Comment(String commentText, User user){
        this.commentText = commentText;
        this.commenter = user;
        time = LocalDateTime.now();
    }

    public String getCommentText() {
        return commentText;
    }

    public User getCommenter() {
        return commenter;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }
}

public class CommentsManager {
    public static HashMap<Integer, ArrayList<Comment>> commentsMap = new HashMap<>();

    public static void addComment(int postID, Comment comment){
        if(commentsMap.containsKey(postID)){
            commentsMap.get(postID).add(comment);
        }else{
            ArrayList<Comment> comments = new ArrayList<>();
            comments.add(comment);
            commentsMap.put(postID, comments);
        }
        saveCommentsToFile();
    }

    public static void removeComment(int postID, Comment comment){
        if(commentsMap.containsKey(postID)){
            commentsMap.get(postID).remove(comment);
        }
    }

    public static ArrayList<Comment> getComments(int postID){
        return commentsMap.get(postID);
    }

    public static void saveCommentsToFile(){
        // Save the commentsMap to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("comments.txt", false))) {
            String line;
            for (int postID : commentsMap.keySet()) {
                for (Comment comment : commentsMap.get(postID)) {
                    line = postID + " -> " + comment.getCommenter().getUsername() + "-separator-" + comment.getCommentText() + "-separator-" + comment.getTime();
                    writer.write(line);
                    writer.newLine();
                }
            }

        }catch (IOException e) {
            System.err.println("Error saving notifications: " + e.getMessage());
        }
    }
    public static void loadCommentsFromFile(){
        // Load the commentsMap from a file
        try (BufferedReader reader = new BufferedReader(new FileReader("comments.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" -> ");
                if (parts.length == 2) {
                    int postID = Integer.parseInt(parts[0]);
                    String[] commentParts = parts[1].split("-separator-");
                    User commenter = UserManager.getFriend(commentParts[0]);
                    String commentText = commentParts[1];
                    LocalDateTime time = LocalDateTime.parse(commentParts[2]);
                    Comment comment = new Comment(commentText, commenter);
                    comment.time = time;
                    if(commentsMap.containsKey(postID)){
                        commentsMap.get(postID).add(comment);
                    }else{
                        ArrayList<Comment> comments = new ArrayList<>();
                        comments.add(comment);
                        commentsMap.put(postID, comments);
                    }
                }
            }
            System.out.println("Comments loaded successfully");
            System.out.println(commentsMap);
        } catch (FileNotFoundException e) {
            System.err.println("No files detected.");
        } catch (IOException e) {
            System.err.println("Error loading from file: " + e.getMessage());
        }
    }

}
