package com.example.ConnectHub;

import javax.print.attribute.UnmodifiableSetException;
import java.io.*;
import java.lang.reflect.Array;
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
        if(!UserManager.curr_user.equals(Home.posts.get(postID).getPoster())){
            UserManager.sendNotification(comment.getCommenter(), Home.posts.get(postID).getPoster(), 3);
        }
//        saveCommentsToFile();
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
        ArrayList<String> lines = new ArrayList<>();
        for (int postID : commentsMap.keySet()) {
            for (Comment comment : commentsMap.get(postID)) {
                String line = postID + " -> " + comment.getCommenter().getUsername() + "-separator-" + comment.getCommentText() + "-separator-" + comment.getTime();
                lines.add(line);
            }
        }
        FilesRW.writeToFile("comments.txt", lines);

    }
    public static void loadCommentsFromFile(){
        // Load the commentsMap from a file
        commentsMap.clear();
        ArrayList<String> data = FilesRW.readFromFile("comments.txt");
        if(data == null) {
            commentsMap = new HashMap<>();
            return;
        }
        for (String line : data) {
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
    }

}
