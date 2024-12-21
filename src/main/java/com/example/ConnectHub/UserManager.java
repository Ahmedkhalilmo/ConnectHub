package com.example.ConnectHub;

import javafx.scene.image.Image;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;

public class UserManager {
    public static List<User> users = new ArrayList<>();
    public static User curr_user;
    private static final String userfilepath = "users.dat";
    protected static String ChatsFilePath = "chats.dat";
    private static final String notificationFilePath = "notifications.txt";
    public static List<Conversation> chats = new ArrayList<>();
    private static Map<User, List<Notification>> notifications = new HashMap<>();


    public static void loadNotifications(){
        try (BufferedReader reader = new BufferedReader(new FileReader(notificationFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" -> ");
                if (parts.length == 2) {
                    User user = UserManager.getFriend(parts[0]);
                    Set<String> curNotifications = new HashSet<>(Arrays.asList(parts[1].split(",")));
                    List<Notification> notificationList = new ArrayList<>();
                    for(String notification : curNotifications){
                        String[] notifData = notification.split("/");
                        Notification notify = new FriendRequestNotification(null, null);
                        System.out.println(1);
                        notify.message = notifData[0];
                        notify.sender = UserManager.getFriend(notifData[1]);
                        notify.timestamp = LocalDateTime.parse(notifData[2]);
                        notificationList.add(notify);
                        System.out.println(2);
                    }
                    notifications.put(user, notificationList);
                    System.out.println(3);
                }
            }
            System.out.println("notifications loaded successfully");
        } catch (FileNotFoundException e) {
            System.err.println("No files detected.");
        } catch (IOException e) {
            System.err.println("Error loading from file: " + e.getMessage());
        }
    }
    public static void saveNotifications(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(notificationFilePath))) {
            for (Map.Entry<User, List<Notification>> entry : notifications.entrySet()) {
                User user = entry.getKey();
                List<Notification> notificationList = entry.getValue();

                StringBuilder notificationsBuilder = new StringBuilder();
                for (Notification notification : notificationList) {
                    notificationsBuilder.append(notification.message)
                            .append("/")
                            .append(notification.sender.getUsername())
                            .append("/")
                            .append(notification.timestamp.toString())
                            .append(",");
                }

                // Remove the trailing comma
                if (notificationsBuilder.length() > 0) {
                    notificationsBuilder.setLength(notificationsBuilder.length() - 1);
                }

                String line = user.getUsername() + " -> " + notificationsBuilder;
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Notifications saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving notifications: " + e.getMessage());
        }
    }

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

    public static User getFriend(String username) {
        for (User user : users) {
            if (username.equals(user.getUsername())) {
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
    public static void sendRequestNotification(User sender, User receiver) {
        Notification notification = new FriendRequestNotification(
                sender.getUsername() + " sent you a friend request!",
                sender
        );
        notifications.computeIfAbsent(receiver, k -> new ArrayList<>()).add(notification);
        saveNotifications();
    }

    public static boolean hasPendingRequest(User sender, User receiver) {
        List<Notification> receiverNotifications = notifications.getOrDefault(receiver, new ArrayList<>());
        for (Notification notification : receiverNotifications) {
            if (notification instanceof FriendRequestNotification &&
                    ((FriendRequestNotification) notification).getSender().equals(sender)) {
                return true;
            }
        }
        return false;
    }

    public static List<Notification> getUserNotifications(User user) {
        return notifications.getOrDefault(user, new ArrayList<>());
    }

    public static void removeNotification(User user, Notification notification) {
        List<Notification> userNotifications = notifications.get(user);
        if (userNotifications != null) {
            userNotifications.remove(notification);
        }
    }

}