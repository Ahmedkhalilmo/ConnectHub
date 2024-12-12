package com.example.ConnectHub;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
public class Conversation implements Serializable{
    public static int Id;
    private int id;
    private String nameOfChat;

    public List<User> getUsersOfConversation() {
        return usersOfConcervation;
    }

    private List<User>usersOfConcervation=new ArrayList<>();


    public List<String> getUsernamesOfParticipants() {
        return UsernamesOfParticipants;
    }

    private List<String> UsernamesOfParticipants = new ArrayList<>();

    public List<Message> getAllMessages() {
        return allMessages;
    }

    private List <Message> allMessages=new ArrayList<>();

    public Conversation(User curr , String nameOfChat) {
        this.usersOfConcervation .add(curr);
        this.nameOfChat=nameOfChat;
        usersOfConcervation.add(curr);
        UsernamesOfParticipants.add(curr.getUsername().toLowerCase());
        id = Id;
        Id++;
    }

    public void sendMessage(User current, Message message){
        allMessages.add(message);
    }
    public void addUser(User user) {
        if(usersOfConcervation.contains(user)){
            System.out.println("user is already exist");
        }
        else
        {
            usersOfConcervation.add(user);
            UsernamesOfParticipants.add(user.getUsername().toLowerCase());
        }
    }
    public void displayConservation(){
        for (Message item:allMessages) {
            item.displayMessage();
        }
    }
    public void showFriends(){
        for (User item:usersOfConcervation) {
            System.out.println(item.getUsername());
        }
    }
    //public void DisplayFriend(User curr){
        //for (User friend: curr.getFriends()) {
           // if(!usersOfConcervation.contains(friend)){
               // System.out.println(friend.getUsername());
            //}

        //}
    //}

    public String getNameOfChat() {
        return nameOfChat;
    }
}
