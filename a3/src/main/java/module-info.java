module com.example.a2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires com.google.gson;
    requires kotlinx.serialization.core;
    requires kotlinx.serialization.json;


    opens com.example.a2 to javafx.fxml;
    exports com.example.a2;
}