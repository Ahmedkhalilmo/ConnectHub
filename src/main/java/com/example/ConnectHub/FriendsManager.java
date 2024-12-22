package com.example.ConnectHub;

import java.io.*;
import java.util.*;

public class FriendsManager {

    // HashMap to represent the graph: user -> set of friends
    public static Map<String, Set<String>> friendsGraph;

    // File to store the friends graph
    private static final String friendsFilePath = "friends_graph.txt";

    // Constructor
    public FriendsManager() {
        friendsGraph = new HashMap<>();
//        loadFromFile();
    }

    // Adds a new friend connection between users
    public static void addFriend(User sender, User receiver) {
        if(sender.getUsername().equals(receiver.getUsername())){
            System.out.println("you can't send friend to your self!.");
        }
        if (UserManager.hasPendingRequest(sender, receiver)) {
            System.out.println("Friend request already sent.");
            return;
        }

        UserManager.sendNotification(sender, receiver,1);
        System.out.println("Notification sent.");
//        saveToFile();
    }

    public static void acceptRequest(User sender, User receiver) {
        String senderUsername = sender.getUsername();
        String receiverUsername = receiver.getUsername();

        friendsGraph.putIfAbsent(senderUsername, new HashSet<>());
        friendsGraph.putIfAbsent(receiverUsername, new HashSet<>());

        friendsGraph.get(senderUsername).add(receiverUsername);
        friendsGraph.get(receiverUsername).add(senderUsername);

        System.out.println("Friend added.");
//        saveToFile();
    }

    // Removes the friends connection
    public void removeFriend(String user1, String user2) {
        if (friendsGraph.containsKey(user1)) {
            friendsGraph.get(user1).remove(user2);
        }
        if (friendsGraph.containsKey(user2)) {
            friendsGraph.get(user2).remove(user1);
        }
//        saveToFile();
    }

    // Returns all friends of the user in a list
    public static List<String> getUserFriends(String user) {
        if(friendsGraph == null){
            friendsGraph = new HashMap<>();
        }
        if (!friendsGraph.containsKey(user)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(friendsGraph.get(user));
    }

    // Writes the graph into the file
    public static void saveToFile() {
        ArrayList<String> lines = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : friendsGraph.entrySet()) {
            lines.add(entry.getKey() + " -> " + String.join(",", entry.getValue()));
        }
        FilesRW.writeToFile(friendsFilePath, lines);
    }


    // Reads the graph from the file
    public static void loadFromFile() {
        ArrayList<String> data = FilesRW.readFromFile(friendsFilePath);
        if(data == null) {
            friendsGraph = new HashMap<>();
            return;
        }
        friendsGraph = new HashMap<>();
        for (String line : data) {
            String[] parts = line.split(" -> ");
            if (parts.length == 2) {
                String user = parts[0];
                Set<String> friends = new HashSet<>(Arrays.asList(parts[1].split(",")));
                friendsGraph.put(user, friends);
            }
        }
    }
}
