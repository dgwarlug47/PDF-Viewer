module com.example.a1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens com.example.a1 to javafx.fxml;
    exports com.example.a1;
}