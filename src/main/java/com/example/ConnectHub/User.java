package com.example.ConnectHub;

import java.io.*;
import java.time.LocalDate;

public class User {
    protected String username;
    protected  String password ;
    protected String email;
    protected String gender;
    protected String date;
    protected LocalDate userBirthDate;
    protected String ImageUrl;
    protected static String relativePath = "src/main/resources/com/example/ConnectHub/Users";

    public User() {
        this.username = null;
        this.password = null;
        this.email = null;
        this.gender = null;
        this.userBirthDate = null;
        this.ImageUrl = null;
        this.date=null;

    }

    public User(String username, String password, String email, String gender, LocalDate userBirthDate, String imageUrl) throws IOException {
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.userBirthDate = userBirthDate;
        this.ImageUrl = imageUrl;

        try{
        BufferedWriter writer=new BufferedWriter(new FileWriter(relativePath+"/"+username+".txt"));
        writer.write(password+"\n"+email+"\n"+gender+"\n"+userBirthDate+"\n"+imageUrl+"\n");
        writer.close();
    }catch (IOException a){
            a.printStackTrace();
        }
    }


}
