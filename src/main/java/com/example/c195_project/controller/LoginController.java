package com.example.c195_project.controller;

import com.example.c195_project.helper.JDBC;
import com.example.c195_project.helper.UserSession;  // Import UserSession class
import com.example.c195_project.model.Appointment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;


import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Controller class responsible for managing the login functionality of the application.
 */
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label locationLabel;

    @FXML
    private Button loginBtn;

    /**
     * Logger instance to log login activity.
     */
    private static final Logger LOGGER = Logger.getLogger("Login Activity");

    static {
        try {
            FileHandler fileHandler = new FileHandler("login_activity.txt", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the UI components. This includes setting localization-based text
     * and setting up default selections.
     */
    @FXML
    private void initialize() {
        ResourceBundle rb = ResourceBundle.getBundle("com.example.c195_project.Login", Locale.getDefault());
        usernameField.setPromptText(rb.getString("username"));
        passwordField.setPromptText(rb.getString("password"));
        locationLabel.setText(rb.getString("location"));
        loginBtn.setText(rb.getString("login"));

        locationLabel.setText(ZoneId.systemDefault().toString());
    }

    /**
     * Handles the login action. Validates the user credentials and logs the user in.
     * Also checks if there are any appointments within the next 15 minutes and notifies the user.
     *
     * @param event the ActionEvent that triggered this method.
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (checkCredentials(username, password)) {
            UserSession.initInstance(username);  // Initialize user session with username

            LOGGER.info("User login successful. User: " + username + ", Time: " + LocalDateTime.now().format(formatter));

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/c195_project/MainWindow.fxml"));

                Parent root = loader.load();

                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/com/example/c195_project/stylesheet.css").toExternalForm());

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();
                stage.setTitle("Main Menu");

                // Check for appointment within the next 15 minutes
                Appointment nextAppointment = getNextAppointmentWithin15Minutes(username);
                if (nextAppointment != null) {
                    String formattedStartTime = formatter.format(nextAppointment.getStart());
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Upcoming Appointment");
                    alert.setHeaderText(null);
                    alert.setContentText("You have an appointment within the next 15 minutes. Appointment ID: " + nextAppointment.getId() + ", Date and time: " + formattedStartTime);
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("No Upcoming Appointments");
                    alert.setHeaderText(null);
                    alert.setContentText("You have no appointments within the next 15 minutes.");
                    alert.showAndWait();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.info("User login failed. User: " + username + ", Time: " + LocalDateTime.now().format(formatter));
            // Handle failed login attempt here...
        }
    }

    /**
     * Validates the provided user credentials against the database.
     *
     * @param username the entered username.
     * @param password the entered password.
     * @return true if the credentials are valid, false otherwise.
     */
    private boolean checkCredentials(String username, String password) {
        String query = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";

        try (PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                } else {
                    ResourceBundle rb = ResourceBundle.getBundle("com.example.c195_project.Login", Locale.getDefault());

                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle(rb.getString("errorTitle"));
                    alert.setHeaderText(null);
                    alert.setContentText(rb.getString("incorrectCredentials"));
                    alert.showAndWait();
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the next appointment of the user (if any) that is scheduled within the next 15 minutes.
     *
     * @param username the username of the logged-in user.
     * @return the next Appointment within the next 15 minutes, or null if there is none.
     */
    private Appointment getNextAppointmentWithin15Minutes(String username) {
        String query = "SELECT * FROM appointments " +
                "JOIN users ON appointments.User_ID = users.User_ID " +
                "WHERE users.User_Name = ? " +
                "AND appointments.Start BETWEEN NOW() AND NOW() + INTERVAL 15 MINUTE " +
                "ORDER BY appointments.Start ASC " +
                "LIMIT 1";

        try (PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
                LocalDateTime createDate = resultSet.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = resultSet.getString("Created_By");
                LocalDateTime lastUpdate = resultSet.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                int customerId = resultSet.getInt("Customer_ID");
                int userId = resultSet.getInt("User_ID");
                int contactId = resultSet.getInt("Contact_ID");

                Appointment appointment = new Appointment(id, title, description, location, type, start, end,
                        createDate, createdBy, lastUpdate, lastUpdatedBy,
                        customerId, userId, contactId);
                return appointment;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;  // Return null if there are no appointments within the next 15 minutes
    }
}