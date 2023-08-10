module com.example.c195_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens com.example.c195_project to javafx.fxml;
    opens com.example.c195_project.model to javafx.base;
    exports com.example.c195_project;
    exports com.example.c195_project.controller;
    opens com.example.c195_project.controller to javafx.fxml;
}
