package com.example.c195_project;

import com.example.c195_project.helper.JDBC;
import com.example.c195_project.dao.UserDAO;

import com.example.c195_project.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * The main application class responsible for launching and managing the application.
 * It initializes the database connection, manages the login process, and controls the main stage.
 */
public class Main extends Application {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());


    private JDBC db;  // encapsulated database connection
    private UserDAO user;  // currently logged-in user

    /**
     * Starts the application, initializes the database connection, and loads the login screen.
     *
     * @param stage The primary stage for this application.
     * @throws IOException if there's an error loading the FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        db = new JDBC();  // initialize the database connection
        db.openConnection();

        ResourceBundle bundle = ResourceBundle.getBundle("com.example.c195_project.Login", Locale.getDefault());
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/c195_project/login.fxml"), bundle);

        Scene scene = new Scene(fxmlLoader.load(), 500, 400);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Executes cleanup operations before the application exits, such as closing the database connection.
     */
    @Override
    public void stop() {
        db.closeConnection();  // close the database connection
    }

    /**
     * The main entry point for the JavaFX application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Validates user credentials and logs the user in if they are correct.
     *
     * @param username The username to validate.
     * @param password The password to validate.
     * @return true if login was successful, false otherwise.
     */
    public boolean login(String username, String password) {
        // validate the user's credentials and set the User object
        // return true if login was successful, false otherwise
        return false;
    }

    /**
     * Logs out the currently logged-in user and returns to the login screen.
     *
     * @param stage The current stage.
     */
    public void logoutAndLogin(Stage stage) {
        // logout the current user and show the login screen
    }
}
