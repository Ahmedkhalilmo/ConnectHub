package com.example.ConnectHub;

import javafx.scene.image.Image;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    public static List<User> users = new ArrayList<>();
    public static User curr_user;
    private static final String userfilepath = "users.dat";
    protected static String ChatsFilePath = "chats.dat";
    public static List<Conversation> chats = new ArrayList<>();

    public static void addUser(User user) {
        users.add(user);
    }

    public static void clearUsers() {
        users.clear();
    }

    public static void saveUsers() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(userfilepath))) {
            out.writeObject(users);
            System.out.println("Users saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadUsers() {
        File file = new File(userfilepath);
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                users = (List<User>) in.readObject();
                System.out.println("Users loaded successfully.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No user data file found. Starting with an empty user list.");
        }
    }

    public static User getUsers(String username, String password) {
        for (User user : users) {
            if (username.equals(user.getUsername()) && Verification.verifyPassword(password, user.getPassword())) {
                curr_user = user;
                return user;
            }
        }
        return null;
    }

    public static void addConversation(Conversation conversation)
    {
        chats.add(conversation);
    }

    public static List<User> getUsers(String username) {
        SearchResults.usersearch.clear();
        for (User user : users) {
            if (username.equals(user.getUsername())) {
                SearchResults.usersearch.add(user);
            }
        }
        return SearchResults.usersearch;
    }
    public static void saveChats() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ChatsFilePath))) {
            out.writeObject(chats);
            System.out.println("Chats saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadChats() {
        File file = new File(ChatsFilePath);
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                chats = (List<Conversation>) in.readObject();
                System.out.println("Chats loaded successfully.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No chat data file found. Starting with an empty chat list.");
        }
    }
}
