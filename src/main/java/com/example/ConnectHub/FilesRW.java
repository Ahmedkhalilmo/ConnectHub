package com.example.ConnectHub;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class FilesRW {

    public static void writeToFile(String filename, ArrayList<String> data) {
        if(data == null) return;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false));
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static ArrayList<String> readFromFile(String filename) {
        ArrayList<String> data = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
