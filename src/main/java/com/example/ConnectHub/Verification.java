package com.example.ConnectHub;



import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Verification {

    private static final Pattern capitalPattern = Pattern.compile("[A-Z]");
    private static final Pattern smallPattern = Pattern.compile("[a-z]");
    private static final Pattern  numberPattern = Pattern.compile("\\d");
    private static final Pattern symbolPattern = Pattern.compile("[^\\w\\s]");
    private static final Pattern emailPattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
    public static boolean hasCapitalLetter(String string) {

        return capitalPattern.matcher(string).find();
    }

    public static boolean hasSmallLetter(String string) {

        return smallPattern.matcher(string).find();
    }

    public static boolean hasNumber(String string) {

        return   numberPattern.matcher(string).find();
    }

    public static boolean hasSymbol(String string) {

        return symbolPattern.matcher(string).find();
    }

    public static boolean hasAll(String string) {
        return hasCapitalLetter(string) && hasSmallLetter(string) && hasNumber(string) && hasSymbol(string);
    }

    public static boolean checkpassword(String password)
    {

        if (hasAll(password))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public static boolean checkemail(String email)
    {
        Matcher mat = emailPattern.matcher(email);
        if(mat.matches())
        {
            return true;
        }

        return false;
    }
    public static boolean isImageFile(File file) {
        try {
            return ImageIO.read(file) != null;
        } catch (IOException e) {
            return false;
        }
    }
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password: " + e.getMessage());
        }
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        String hashedPlainPassword = hashPassword(plainPassword);
        return hashedPlainPassword.equals(hashedPassword);
    }
}
 