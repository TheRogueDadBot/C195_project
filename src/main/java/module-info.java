module com.example.c195_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.c195_project to javafx.fxml;
    exports com.example.c195_project;
}