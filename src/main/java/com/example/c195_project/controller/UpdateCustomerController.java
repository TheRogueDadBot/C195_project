package com.example.c195_project.controller;

import com.example.c195_project.helper.Helper;
import com.example.c195_project.model.Customer;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Controller class responsible for managing the update customer functionality of the application.
 */
public class UpdateCustomerController {

    private static final Logger LOGGER = Logger.getLogger(UpdateCustomerController.class.getName());


    private static final String MAIN_WINDOW_FXML = "/com/example/c195_project/MainWindow.fxml";
    private static final String STYLESHEET_CSS = "/com/example/c195_project/stylesheet.css";
    private static final String MAIN_MENU_TITLE = "Main Menu";
    private static final String UPDATE_FAIL_MESSAGE = "Failed to update customer.";

    @FXML
    private TextField updateCustIdTxt;

    @FXML
    private TextField updateCustNameTxt;

    @FXML
    private TextField updateCustAddressTxt;

    @FXML
    private TextField updateCustPhoneNumberTxt;

    @FXML
    private ComboBox<String> updateCustCountryCBox;

    @FXML
    private ComboBox<String> updateCustStateCBox;

    @FXML
    private TextField updateCustPostalTxt;

    @FXML
    private Button cancelBtn;

    /**
     * Initializes the UI components. Sets up available options and default selections.
     */
    @FXML
    public void initialize() {
        // Add change listener to updateCustCountryCBox
        updateCustCountryCBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            // When the selected country changes, update the divisions in updateCustStateCBox
            List<String> divisions = Helper.getDivisions(newValue);
            updateCustStateCBox.setItems(FXCollections.observableArrayList(divisions));
        });
    }

    /**
     * Populates the form fields with the details of the customer to be updated.
     *
     * @param customer the Customer object containing the details to populate the form.
     */
    public void setCustomer(Customer customer) {
        // Set the fields with the customer data
        updateCustIdTxt.setText(Integer.toString(customer.getId()));
        updateCustIdTxt.setDisable(true);
        updateCustIdTxt.setStyle("-fx-text-fill: grey;");
        updateCustNameTxt.setText(customer.getName());
        updateCustAddressTxt.setText(customer.getAddress());
        updateCustPhoneNumberTxt.setText(customer.getPhone());
        updateCustPostalTxt.setText(customer.getPostalCode());

        String divisionName = Helper.getDivisionData(customer.getDivisionId());
        String countryName = Helper.getCountryData(customer.getDivisionId());

        List<String> allCountries = Helper.getAllCountryNames();

        // Set the items in the country ComboBox and select the correct country
        updateCustCountryCBox.setItems(FXCollections.observableArrayList(allCountries));
        updateCustCountryCBox.getSelectionModel().select(countryName);

        // Request the ComboBox to refresh its items by triggering a change
        updateCustCountryCBox.getSelectionModel().selectedItemProperty().get();

        // Set the selected division in the division ComboBox
        updateCustStateCBox.getSelectionModel().select(divisionName);
    }



    /**
     * Handles the action of the "Save" button. Gathers the data from the form fields,
     * updates the customer in the database, and handles the success or failure of the update.
     * Ensures:
     * - All required fields are filled.
     * - Phone number format is valid.
     * - A valid country and division are selected.
     */
    @FXML
    private void updateCustSaveBtnHandler() {
        // Get the updated values from the text fields and combo boxes
        int id = Integer.parseInt(updateCustIdTxt.getText());
        String name = updateCustNameTxt.getText();
        String address = updateCustAddressTxt.getText();
        String phoneNumber = updateCustPhoneNumberTxt.getText();
        String postalCode = updateCustPostalTxt.getText();
        String countryName = updateCustCountryCBox.getValue();
        String divisionName = updateCustStateCBox.getValue();

        // Check if all required fields are filled
        if (name.trim().isEmpty() || address.trim().isEmpty() || phoneNumber.trim().isEmpty() || postalCode.trim().isEmpty() ||
                countryName == null || divisionName == null) {
            Helper.showAlert("All fields are required.");
            return;
        }

        // Convert the country and division names to IDs
        int divisionId = Helper.getDivisionId(countryName, divisionName);
        if (divisionId == -1) {
            Helper.showAlert("Selected division is not valid for the provided country.");
            return;
        }

        // Update the customer in the database
        if (Helper.updateCustomer(id, name, address, postalCode, phoneNumber, divisionId)) {
            // If the update was successful, load the main window
            goToMainWindow();
        } else {
            // If the update failed, show an error message
            Helper.showAlert(UPDATE_FAIL_MESSAGE);
        }
    }


    /**
     * Redirects the user to the main window after updating the customer.
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
