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

    // add new friend connection between users
    public void addFriend(String user1, String user2) {
        // Add user1 and user2 to the graph if they don't exist
        System.out.println("started");
        friendsGraph.putIfAbsent(user1, new HashSet<>());

        System.out.println("initialzed");
        // Add the friendship connection
        friendsGraph.get(user1).add(user2);

        System.out.println("friend added");
        saveToFile();
    }

    // removes the friends connection
    public void removeFriend(String user1, String user2) {
        if (friendsGraph.containsKey(user1)) {
            friendsGraph.get(user1).remove(user2);
        }
        saveToFile();
    }

    // return all friends of the user in a list
    public List<String> getUserFriends(String user) {
        if (!friendsGraph.containsKey(user)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(friendsGraph.get(user));
    }

    // writes the graph into file
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

    // reads the graph from the file
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
            System.out.println(UserManager.curr_user.getUsername() + " " +
                    friendsGraph.get(UserManager.curr_user.getUsername()));
        } catch (FileNotFoundException e) {
            // File does not exist, no data to load
            System.err.println("no files detected");
        } catch (IOException e) {
            System.err.println("Error loading from file: " + e.getMessage());
        }
    }
}