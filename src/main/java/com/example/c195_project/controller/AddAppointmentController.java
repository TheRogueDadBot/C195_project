package com.example.c195_project.controller;

import com.example.c195_project.dao.AppointmentDAO;
import com.example.c195_project.helper.Helper;
import com.example.c195_project.model.Appointment;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Controller class responsible for handling the "Add Appointment" UI functionality.
 */
public class AddAppointmentController {

    private static final Logger LOGGER = Logger.getLogger(AddAppointmentController.class.getName());

    private static final String MAIN_WINDOW_FXML = "/com/example/c195_project/MainWindow.fxml";
    private static final String STYLESHEET_CSS = "/com/example/c195_project/stylesheet.css";
    private static final String TIME_PATTERN = "HH:mm";
    private static final int TIME_SLOT_INTERVAL = 15;
    private static final String SAVE_FAILURE_MESSAGE = "Failed to add appointment.";
    private static final String TIME_SLOT_CONFLICT_MESSAGE = "Appointment overlaps with an existing appointment for the customer.";

    @FXML
    private Button cancelBtn;
    @FXML
    private ComboBox<String> addAppContactCBox;
    @FXML
    private ComboBox<Integer> addAppCustIdCBox;
    @FXML
    private ComboBox<Integer> addAppUserIdCBox;
    @FXML
    private ComboBox<String> addAppStartCBox;
    @FXML
    private ComboBox<String> addAppEndCBox;
    @FXML
    private TextField addAppIdTxt;
    @FXML
    private TextField addAppTitleTxt;
    @FXML
    private TextField addAppTypeTxt;
    @FXML
    private TextField addAppDescriptionTxt;
    @FXML
    private TextField addAppLocationTxt;
    @FXML
    private DatePicker addAppStartDPick;
    @FXML
    private DatePicker addAppEndDPick;

    private AppointmentDAO appointmentDAO = new AppointmentDAO();

    /**
     * Initializes the view by setting up combo boxes, default values, and event listeners.
     */
    public void initialize() {
        cancelBtn.setOnAction(e -> loadFXMLScene(MAIN_WINDOW_FXML));

        addAppContactCBox.setItems(FXCollections.observableArrayList(Helper.getAllContactNames()));
        addAppCustIdCBox.setItems(FXCollections.observableArrayList(getSortedCustomerIds()));
        addAppUserIdCBox.setItems(FXCollections.observableArrayList(getSortedUserIds()));

        // Define the Eastern Standard Time zone
        ZoneId estZone = ZoneId.of("America/New_York");

// Define the start and end times in EST
        ZonedDateTime estStart = ZonedDateTime.of(LocalDate.now(), LocalTime.of(8, 0), estZone);
        ZonedDateTime estEnd = ZonedDateTime.of(LocalDate.now(), LocalTime.of(22, 0), estZone);

// Convert the EST times to the system's default time zone
        ZonedDateTime localStart = estStart.withZoneSameInstant(ZoneId.systemDefault());
        ZonedDateTime localEnd = estEnd.withZoneSameInstant(ZoneId.systemDefault());

// Generate the time slots
        List<String> timeSlots = generateTimeSlots(localStart.toLocalTime(), localEnd.toLocalTime(), 15);

// Set the time slots in the combo boxes
        addAppStartCBox.setItems(FXCollections.observableArrayList(timeSlots));
        addAppEndCBox.setItems(FXCollections.observableArrayList(timeSlots));


        addAppIdTxt.setText(Integer.toString(appointmentDAO.getNextAppointmentId()));
        disableAndGreyOutTextField(addAppIdTxt);
    }

    /**
     * Navigates the user back to the main window.
     */
    @FXML
    private void goToMainWindow() {
        loadFXMLScene(MAIN_WINDOW_FXML);
    }

    /**
     * Handles the save button click event for adding a new appointment.
     * <p>
     * Validates the input based on several criteria:
     * - Checks if all required fields are filled.
     * - Ensures the end date/time is after the start date/time.
     * - Ensures the appointment doesn't exceed 8 hours.
     * - Ensures the appointment is scheduled during business hours (8:00 am to 5:00 pm).
     * - Checks if the entered contact name exists.
     * - Checks if the appointment time slot is free for the selected customer.
     * </p>
     * If all validations pass, it adds the appointment to the database.
     */
    @FXML
    private void addAppSaveBtnHandler() {
        // Required fields check
        if (addAppTitleTxt.getText().trim().isEmpty() ||
                addAppTypeTxt.getText().trim().isEmpty() ||
                addAppDescriptionTxt.getText().trim().isEmpty() ||
                addAppLocationTxt.getText().trim().isEmpty()) {
            Helper.showAlert("All fields are required.");
            return;
        }

        LocalDateTime startDateTime = getLocalDateTimeFromDatePickerAndComboBox(addAppStartDPick, addAppStartCBox);
        LocalDateTime endDateTime = getLocalDateTimeFromDatePickerAndComboBox(addAppEndDPick, addAppEndCBox);

        // End Date/Time Check
        if (endDateTime.isBefore(startDateTime) || endDateTime.isEqual(startDateTime)) {
            Helper.showAlert("End date/time must be after the start date/time.");
            return;
        }

        // Duration Check (not more than 8 hours)
        if (Duration.between(startDateTime, endDateTime).toHours() > 8) {
            Helper.showAlert("Appointments cannot be longer than 8 hours.");
            return;
        }

        // Existing checks
        if (!Helper.getAllContactNames().contains(addAppContactCBox.getValue())) {
            Helper.showAlert("Invalid contact name");
            return;
        }

        Appointment newAppointment = createNewAppointment();

        if (appointmentDAO.isTimeSlotFree(newAppointment.getCustomerId(), newAppointment.getStart(), newAppointment.getEnd(), null)) {
            if (appointmentDAO.addAppointment(newAppointment)) {
                loadFXMLScene(MAIN_WINDOW_FXML);
            } else {
                Helper.showAlert(SAVE_FAILURE_MESSAGE);
            }
        } else {
            Helper.showAlert(TIME_SLOT_CONFLICT_MESSAGE);
        }
    }


    /**
     * Retrieves and sorts customer IDs.
     * @return A list of sorted customer IDs.
     */
    private List<Integer> getSortedCustomerIds() {
        List<Integer> customerIds = Helper.getAllCustomerIds();
        Collections.sort(customerIds);
        return customerIds;
    }

    /**
     * Retrieves and sorts user IDs.
     * @return A list of sorted user IDs.
     */
    private List<Integer> getSortedUserIds() {
        List<Integer> userIds = Helper.getAllUserIds();
        Collections.sort(userIds);
        return userIds;
    }

    /**
     * Generates time slots between the given start and end times.
     * @param start Start time.
     * @param end End time.
     * @param intervalMinutes Interval in minutes for each time slot.
     * @return A list of time slots as strings.
     */
    public static List<String> generateTimeSlots(LocalTime start, LocalTime end, int intervalMinutes) {
        List<String> slots = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(TIME_PATTERN);

        while (!start.isAfter(end)) {
            slots.add(dtf.format(start));
            start = start.plusMinutes(intervalMinutes);
        }

        return slots;
    }

    /**
     * Creates a new appointment instance based on the user input.
     * @return An instance of the Appointment class.
     */
    private Appointment createNewAppointment() {
        int id = Integer.parseInt(addAppIdTxt.getText());
        String title = addAppTitleTxt.getText();
        String type = addAppTypeTxt.getText();
        String description = addAppDescriptionTxt.getText();
        String location = addAppLocationTxt.getText();

        LocalDateTime startDateTime = getLocalDateTimeFromDatePickerAndComboBox(addAppStartDPick, addAppStartCBox);
        LocalDateTime endDateTime = getLocalDateTimeFromDatePickerAndComboBox(addAppEndDPick, addAppEndCBox);

        int customerId = addAppCustIdCBox.getValue();
        int userId = addAppUserIdCBox.getValue();
        int contactId = Helper.getContactId(addAppContactCBox.getValue());

        // Create a new Appointment object with the local times
        return new Appointment(id, title, description, location, type, startDateTime, endDateTime, LocalDateTime.now(), "system", LocalDateTime.now(), "system", customerId, userId, contactId);
    }

    /**
     * Converts date and time values from a DatePicker and ComboBox into a LocalDateTime.
     * @param datePicker DatePicker containing the date.
     * @param comboBox ComboBox containing the time.
     * @return A LocalDateTime combining the date and time values.
     */
    private LocalDateTime getLocalDateTimeFromDatePickerAndComboBox(DatePicker datePicker, ComboBox<String> comboBox) {
        LocalTime localTime = LocalTime.parse(comboBox.getValue(), DateTimeFormatter.ofPattern(TIME_PATTERN));
        return LocalDateTime.of(datePicker.getValue(), localTime);
    }

    /**
     * Disables and grays out the given text field.
     * @param textField The TextField to be disabled and styled.
     */
    private void disableAndGreyOutTextField(TextField textField) {
        textField.setDisable(true);
        textField.setStyle("-fx-text-fill: grey;");
    }

    /**
     * Loads a new FXML scene.
     * @param fxmlPath Path to the FXML file to be loaded.
     */
    private void loadFXMLScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent sceneRoot = loader.load();
            Scene scene = new Scene(sceneRoot);
            scene.getStylesheets().add(getClass().getResource(STYLESHEET_CSS).toExternalForm());

            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            stage.setTitle("Main Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
