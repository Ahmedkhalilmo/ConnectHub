module com.example.facebook {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.ConnectHub to javafx.fxml;
    exports com.example.ConnectHub;
}