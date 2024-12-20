package com.example.ConnectHub;

import java.io.*;
import java.util.*;

public class FriendsManager {

    // HashMap to represent the graph: user -> set of friends
    private Map<String, Set<String>> friendsGraph;

    // File to store the friends graph
    private static final String FILE_NAME = "friends_graph.txt";

    // Constructor
    public FriendsManager() {
        friendsGraph = new HashMap<>();
        loadFromFile();
    }

    // Adds a new friend connection between users
    public void addFriend(User sender, User receiver) {
        if (UserManager.hasPendingRequest(sender, receiver)) {
            System.out.println("Friend request already sent.");
            return;
        }

        UserManager.sendRequestNotification(sender, receiver);
        System.out.println("Notification sent.");
        saveToFile();
    }

    public void acceptRequest(User sender, User receiver) {
        String senderUsername = sender.getUsername();
        String receiverUsername = receiver.getUsername();

        friendsGraph.putIfAbsent(senderUsername, new HashSet<>());
        friendsGraph.putIfAbsent(receiverUsername, new HashSet<>());

        friendsGraph.get(senderUsername).add(receiverUsername);
        friendsGraph.get(receiverUsername).add(senderUsername);

        System.out.println("Friend added.");
        saveToFile();
    }

    // Removes the friends connection
    public void removeFriend(String user1, String user2) {
        if (friendsGraph.containsKey(user1)) {
            friendsGraph.get(user1).remove(user2);
        }
        if (friendsGraph.containsKey(user2)) {
            friendsGraph.get(user2).remove(user1);
        }
        saveToFile();
    }

    // Returns all friends of the user in a list
    public List<String> getUserFriends(String user) {
        if (!friendsGraph.containsKey(user)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(friendsGraph.get(user));
    }

    // Writes the graph into the file
    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, Set<String>> entry : friendsGraph.entrySet()) {
                writer.write(entry.getKey() + " -> " + String.join(",", entry.getValue()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }

    // Reads the graph from the file
    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" -> ");
                if (parts.length == 2) {
                    String user = parts[0];
                    Set<String> friends = new HashSet<>(Arrays.asList(parts[1].split(",")));
                    friendsGraph.put(user, friends);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("No files detected.");
        } catch (IOException e) {
            System.err.println("Error loading from file: " + e.getMessage());
        }
    }
}
