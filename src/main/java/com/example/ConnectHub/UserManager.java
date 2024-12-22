package com.example.ConnectHub;

import javafx.scene.image.Image;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;

public class UserManager {
    public static ArrayList<User> users = new ArrayList<>();
    public static User curr_user;
    private static final String userfilepath = "users.dat";
    protected static String ChatsFilePath = "chats.dat";
    private static final String notificationFilePath = "notifications.txt";
    public static List<Conversation> chats = new ArrayList<>();
    public static Map<User, List<Notification>> notifications = new HashMap<>();


    public static void loadNotifications() {
        ArrayList<String> lines = FilesRW.readFromFile(notificationFilePath);
        if(lines == null) {
            notifications = new HashMap<>();
            return;
        }
        for (String line : lines) {
            String[] parts = line.split(" -> ");
            if (parts.length == 2) {
                User user = UserManager.getFriend(parts[0]);
                Set<String> curNotifications = new HashSet<>(Arrays.asList(parts[1].split(",")));
                List<Notification> notificationList = new ArrayList<>();
                for (String notification : curNotifications) {
                    String[] notifData = notification.split("/");
                    Notification notify;
                    if(notifData[2].equals("1"))
                        notify = new FriendRequestNotification(null, null);
                    else
                        notify = new ReactNotification(null, null, 2);
                    notify.message = notifData[0];
                    notify.sender = UserManager.getFriend(notifData[1]);
                    notify.type = Integer.parseInt(notifData[2]);
                    notify.timestamp = LocalDateTime.parse(notifData[3]);
                    notificationList.add(notify);
                }
                notifications.put(user, notificationList);
            }
        }
    }

    public static void saveNotifications() {
        ArrayList<String> lines = new ArrayList<>();
        for (Map.Entry<User, List<Notification>> entry : notifications.entrySet()) {
            User user = entry.getKey();
            List<Notification> notificationList = entry.getValue();

            StringBuilder notificationsBuilder = new StringBuilder();
            for (Notification notification : notificationList) {
                notificationsBuilder.append(notification.message)
                        .append("/")
                        .append(notification.sender.getUsername())
                        .append("/")
                        .append(notification.type)
                        .append("/")
                        .append(notification.timestamp.toString())
                        .append(",");
            }
            // Remove the trailing comma
            if (notificationsBuilder.length() > 0) {
                notificationsBuilder.setLength(notificationsBuilder.length() - 1);
            }

            String line = user.getUsername() + " -> " + notificationsBuilder;
            lines.add(line);
        }
        FilesRW.writeToFile(notificationFilePath, lines);
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
                users = (ArrayList<User>) in.readObject();
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

    public static void addConversation(Conversation conversation) {
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

    public static void sendNotification(User sender, User receiver, int type) {
        Notification notification;
        if(type == 1) notification = new FriendRequestNotification(null, sender);
        else notification = new ReactNotification(null, sender, type);

        if (type == 1) {
            notification.message =sender.getUsername() + " Sent You a Connection Request";
        }
        else if (type == 2) {
            notification.message =sender.getUsername() + " Liked Your Post";
        }
        else if (type == 3) {
            notification.message =sender.getUsername() + " Commented Your Post";
        }
        else if (type == 4) {
            notification.message =sender.getUsername() + " Tagged You In Post";
        }
        notifications.computeIfAbsent(receiver, k -> new ArrayList<>()).add(notification);
//        saveNotifications();
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
//        saveNotifications();
    }

}