module com.example.code {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.media;


    opens com.example.code to javafx.fxml;
    exports com.example.code;
}