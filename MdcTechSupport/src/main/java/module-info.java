module com.example.mdctechsupport {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires java.desktop;


    opens com.example.mdctechsupport to javafx.fxml;
    exports com.example.mdctechsupport;
}