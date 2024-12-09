package com.example.ConnectHub;

import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

public class UserManager extends User{


    public static boolean userFound(String username) {

            File userFile = new File(relativePath + "/" + username + ".txt");
            if (userFile.exists()) {
                return true;
            }else{
            return false;
            }
    }
    public void UserData(String username) {
        try {
            this.username=username;
            BufferedReader reader = new BufferedReader(new FileReader(relativePath + "/" + username + ".txt"));
            this.password=reader.readLine();
            this.email=reader.readLine();

            this.gender = reader.readLine();
            this.date=reader.readLine();
            this.ImageUrl =reader.readLine();
            System.out.println(username+password+email+gender+date+ImageUrl);
                    reader.close();
        } catch (IOException e) {

            e.printStackTrace();

        }
    }

}
