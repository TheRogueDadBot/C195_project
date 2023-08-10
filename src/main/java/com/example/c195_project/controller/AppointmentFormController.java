package com.example.c195_project.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;

import com.example.c195_project.model.Appointment;

/**
 * Controller class responsible for handling the "Appointment Form" UI functionality.
 */
public class AppointmentFormController {

    @FXML
    private RadioButton allAppointmentsButton;
    @FXML
    private RadioButton monthlyAppointmentsButton;
    @FXML
    private RadioButton weeklyAppointmentsButton;
    @FXML
    private TableView<Appointment> appointmentsTable;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;
    @FXML
    private TableColumn<Appointment, Integer> contactIdColumn;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button addAppointmentButton;
    @FXML
    private Button modifyAppointmentButton;
    @FXML
    private Button deleteAppointmentButton;
    @FXML
    private ComboBox<String> contactNameComboBox;
    @FXML
    private ComboBox<String> customerNameComboBox;
    @FXML
    private ComboBox<String> startTimeComboBox;
    @FXML
    private ComboBox<String> endTimeComboBox;
    @FXML
    private DatePicker datePicker;

    /**
     * Initializes the UI components, setting up button groups and loading data into
     * the table view and combo boxes.
     */
    @FXML
    public void initialize() {
        ToggleGroup group = new ToggleGroup();
        allAppointmentsButton.setToggleGroup(group);
        monthlyAppointmentsButton.setToggleGroup(group);
        weeklyAppointmentsButton.setToggleGroup(group);

        // Example of setting up the columns in the table view
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        contactIdColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));

        // You would need to implement these methods to fetch and display data
        loadAppointments();
        loadContacts();
        loadCustomers();
        loadTimes();
    }

    /**
     * Loads the appointments from the database and displays them in the table view.
     */
    private void loadAppointments() {
        // This method should fetch appointments from the database and add them to the table view
        // Here is a basic example using a placeholder ObservableList
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        // fetch data and add to appointments...
        appointmentsTable.setItems(appointments);
    }

    /**
     * Loads the contacts from the database and populates the contacts combo box.
     */
    private void loadContacts() {
        // This method should fetch contact names from the database and add them to the combo box
        // Here is a basic example using a placeholder ObservableList
        ObservableList<String> contacts = FXCollections.observableArrayList();
        // fetch data and add to contacts...
        contactNameComboBox.setItems(contacts);
    }

    /**
     * Loads the customers from the database and populates the customers combo box.
     */
    private void loadCustomers() {
        // This method should fetch customer names from the database and add them to the combo box
        // Here is a basic example using a placeholder ObservableList
        ObservableList<String> customers = FXCollections.observableArrayList();
        // fetch data and add to customers...
        customerNameComboBox.setItems(customers);
    }

    /**
     * Loads and sets the available times for appointments in the start and end time combo boxes.
     */
    private void loadTimes() {
        // This method should populate the start and end time combo boxes
        // Here is a basic example using a placeholder ObservableList
        ObservableList<String> times = FXCollections.observableArrayList();
        // populate times...
        startTimeComboBox.setItems(times);
        endTimeComboBox.setItems(times);
    }

    @FXML
    private void handlePreviousButtonAction() {
        // This method should handle the action of the previous button
    }

    @FXML
    private void handleNextButtonAction() {
        // This method should handle the action of the next button
    }

    @FXML
    private void handleAddAppointmentButtonAction() {
        // This method should handle the action of the add appointment button
    }

    @FXML
    private void handleModifyAppointmentButtonAction() {
        // This method should handle the action of the modify appointment button
    }

    @FXML
    private void handleDeleteAppointmentButtonAction() {
        // This method should handle the action of the delete appointment button
    }
}