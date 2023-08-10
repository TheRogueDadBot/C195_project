package com.example.c195_project.controller;

import com.example.c195_project.helper.Helper;
import com.example.c195_project.helper.JDBC;
import com.example.c195_project.helper.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.logging.Logger;

/**
 * Controller class responsible for handling the "Add Customer" UI functionality.
 */
public class AddCustomerController {

    private static final Logger LOGGER = Logger.getLogger(AddCustomerController.class.getName());


    private static final String MAIN_WINDOW_FXML = "/com/example/c195_project/MainWindow.fxml";
    private static final String STYLESHEET_CSS = "/com/example/c195_project/stylesheet.css";
    private static final String MAIN_MENU_TITLE = "Main Menu";
    private static final String ERROR_MESSAGE = "Error: ";
    private static final String DIVISION_NOT_FOUND_MESSAGE = "Error: Division not found in the database.";

    @FXML
    private Button cancelBtn;
    @FXML
    private ComboBox<String> addCustCountryCBox;
    @FXML
    private ComboBox<String> addCustStateCBox;
    @FXML
    private TextField addCustIdTxt;
    @FXML
    private Button saveBtn;
    @FXML
    private TextField addCustNameTxt;
    @FXML
    private TextField addCustAddressTxt;
    @FXML
    private TextField addCustPostalTxt;
    @FXML
    private TextField addCustPhoneTxt;

    /**
     * Initializes the view by setting up combo boxes, default values, and event listeners.
     * Populates countries and divisions combo boxes and sets up listeners.
     */
    public void initialize() {
        int nextCustomerId = Helper.getNextCustomerId();
        addCustIdTxt.setText(Integer.toString(nextCustomerId));
        addCustIdTxt.setEditable(false);
        addCustIdTxt.setStyle("-fx-text-fill: grey;");
        ObservableList<String> countries = FXCollections.observableArrayList("U.S", "UK", "Canada");
        addCustCountryCBox.setItems(countries);

        addCustCountryCBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            ObservableList<String> divisions = FXCollections.observableArrayList(Helper.getDivisions(newValue));
            addCustStateCBox.setItems(divisions);
        });
    }

    /**
     * Handles the save button click event for adding a new customer.
     * Validates the input based on several criteria:
     * - Checks if all required fields are filled.
     * - Validates the phone number format.
     * - Ensures a valid country and division are selected.
     * If all validations pass, it adds the customer to the database.
     */
    @FXML
    private void saveCustomer() {
        try {
            String name = addCustNameTxt.getText();
            String address = addCustAddressTxt.getText();
            String postalCode = addCustPostalTxt.getText();
            String phone = addCustPhoneTxt.getText();
            String countryName = addCustCountryCBox.getValue();
            String divisionName = addCustStateCBox.getValue();

            // Required fields check
            if (name.trim().isEmpty() || address.trim().isEmpty() || postalCode.trim().isEmpty() || phone.trim().isEmpty() ||
                    countryName == null || divisionName == null) {
                Helper.showAlert("All fields are required.");
                return;
            }

            int divisionId = Helper.getDivisionId(countryName, divisionName);
            if (divisionId == -1) {
                Helper.showAlert(DIVISION_NOT_FOUND_MESSAGE);
                return;
            }

            LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

            String query = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = JDBC.getConnection().prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, address);
            statement.setString(3, postalCode);
            statement.setString(4, phone);
            statement.setTimestamp(5, Timestamp.valueOf(now));

            String createdBy = UserSession.getInstance().getUserName();
            statement.setString(6, createdBy);

            statement.setTimestamp(7, Timestamp.valueOf(now));
            statement.setString(8, createdBy);

            statement.setInt(9, divisionId);

            statement.execute();
            goToMainWindow();
        } catch (SQLException e) {
            Helper.showAlert(ERROR_MESSAGE + e.getMessage());
        }
    }


    /**
     * Navigates the user back to the main window.
     */
    @FXML
    private void goToMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_WINDOW_FXML));
            Parent mainWindow = loader.load();

            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            Scene scene = new Scene(mainWindow);
            scene.getStylesheets().add(getClass().getResource(STYLESHEET_CSS).toExternalForm());

            stage.setScene(scene);
            stage.show();
            stage.setTitle(MAIN_MENU_TITLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}