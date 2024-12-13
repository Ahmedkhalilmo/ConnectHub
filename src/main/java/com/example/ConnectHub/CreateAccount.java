package com.example.ConnectHub;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.util.Objects;


public class CreateAccount {

    @FXML
    TextField usernameF;
    @FXML
    PasswordField passwordF;

    @FXML
    PasswordField passwordF2;
    @FXML
    Label ErrorLabel;
    @FXML
    TextField emailF;
    @FXML
    RadioButton MaleF;
    @FXML
    RadioButton FemaleF;
    @FXML
    DatePicker DateF;
    @FXML
    private ToggleGroup genderGroup;
    @FXML
    public void initialize() {
        genderGroup = new ToggleGroup();
        MaleF.setToggleGroup(genderGroup);
        FemaleF.setToggleGroup(genderGroup);
    }

    @FXML
    Circle ProfileImageView;
    private String ImageUrl;
    private Path currentImagePath;
    private Stage stage;
    private Scene scene;
    private Parent root;
    public void setLogin(ActionEvent e) throws IOException {
        if(currentImagePath!= null)
        {
            Files.delete(currentImagePath);
        }
        root = FXMLLoader.load(this.getClass().getResource("Login.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene=new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Login.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    public void setCreateAccount(ActionEvent e) throws IOException {

            String username = usernameF.getText();
            String password = passwordF.getText();
            String password2 = passwordF2.getText();
            String email = emailF.getText();
            String gender = "";
            LocalDate userBirthDate = DateF.getValue();

            if (MaleF.isSelected()) {
                gender = "Male";
            } else if (FemaleF.isSelected()) {
                gender = "Female";
            }

            if (username.isEmpty()) {
                ErrorLabel.setText("Please enter your Full name");
                return;
            }
            if (email.isEmpty()) {
                ErrorLabel.setText("Please enter your email");
                return;
            }
            if (!Verification.checkemail(email)) {
                ErrorLabel.setText("Please enter a valid email");
                return;
            }
            for(User user:UserManager.users)
                {
                if(user.getUsername().equals(username))
                {
                ErrorLabel.setText("Username already taken");
                return;
                 }
             }
            if (password.isEmpty()) {
                ErrorLabel.setText("Please enter your password");
                return;
            }
            if (password2.isEmpty()) {
                ErrorLabel.setText("Please confirm your password");
                return;
            }
            if (gender.isEmpty()) {
                ErrorLabel.setText("Please choose a gender");
                return;
            }
            if (userBirthDate == null) {
                ErrorLabel.setText("Please enter a birthdate");
                return;
            }
            if (!password.equals(password2)) {
                ErrorLabel.setText("Passwords don't match!");
                return;
            }
            if (!Verification.checkpassword(password)) {
                ErrorLabel.setText("Your password needs to contain \n a capital letter, small letter, number, and symbol");
                return;
            }
            if (currentImagePath == null) {
                ErrorLabel.setText("Please choose a profile picture");
                return;
            }
        String hashedPassword = Verification.hashPassword(password);

             User newUser = new User(username, hashedPassword, email, gender, userBirthDate, ImageUrl);
             UserManager.addUser(newUser);
             UserManager.saveUsers();

        User NewUser = new User(username,password,email,gender,userBirthDate,ImageUrl);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(" SignUp successful --> Now Login ");
        if(alert.showAndWait().get() == ButtonType.OK || alert.showAndWait().get() == ButtonType.CANCEL)
        {
            Parent root = FXMLLoader.load(this.getClass().getResource("Login.fxml"));
            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            scene=new Scene(root);
            scene.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        }


    }
    public void setImageUrl(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {

                String relativePath = "src/main/resources/com/example/ConnectHub/ProfileImages";

                Path from = Paths.get(selectedFile.toURI());
                Path to = Paths.get(relativePath, selectedFile.getName());
                if(!Files.exists(to))
                {
                    Files.copy(from, to);
                }
                ImageUrl = "ProfileImages/" + selectedFile.getName();
                System.out.println(ImageUrl);
                Image image = new Image(selectedFile.toURI().toString());
                ProfileImageView.setFill(new ImagePattern(image));
                currentImagePath = to;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
