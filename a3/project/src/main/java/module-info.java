module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;
                requires kotlin.stdlib;
    
                            
    opens com.example.project to javafx.fxml;
    exports com.example.project;
}