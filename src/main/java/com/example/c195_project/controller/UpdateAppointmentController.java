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
 * Controller class responsible for managing the update appointment functionality of the application.
 */
public class UpdateAppointmentController {

    private static final Logger LOGGER = Logger.getLogger(UpdateAppointmentController.class.getName());

    private static final String MAIN_WINDOW_FXML = "/com/example/c195_project/MainWindow.fxml";
    private static final String STYLESHEET_CSS = "/com/example/c195_project/stylesheet.css";
    private static final String TIME_PATTERN = "HH:mm";
    private static final String OVERLAPPING_SCHEDULE_MESSAGE = "There is an overlap in scheduling times for this customer.";

    @FXML
    private Button cancelBtn;
    @FXML
    private ComboBox<String> updateAppContactCBox;
    @FXML
    private ComboBox<Integer> updateAppCustIdCBox;
    @FXML
    private ComboBox<Integer> updateAppUserIdCBox;
    @FXML
    private ComboBox<String> updateAppStartCBox;
    @FXML
    private ComboBox<String> updateAppEndCBox;
    @FXML
    private TextField updateAppIdTxt;
    @FXML
    private TextField updateAppTitleTxt;
    @FXML
    private TextField updateAppTypeTxt;
    @FXML
    private TextField updateAppDescriptionTxt;
    @FXML
    private TextField updateAppLocationTxt;
    @FXML
    private DatePicker updateAppStartDPick;
    @FXML
    private DatePicker updateAppEndDPick;

    private AppointmentDAO appointmentDAO = new AppointmentDAO();

    /**
     * Initializes the UI components. Sets up available options and default selections.
     */
    public void initialize() {
        cancelBtn.setOnAction(e -> goToMainWindow());

        updateAppContactCBox.setItems(FXCollections.observableArrayList(Helper.getAllContactNames()));
        updateAppCustIdCBox.setItems(FXCollections.observableArrayList(getSortedCustomerIds()));
        updateAppUserIdCBox.setItems(FXCollections.observableArrayList(getSortedUserIds()));

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
        updateAppStartCBox.setItems(FXCollections.observableArrayList(timeSlots));
        updateAppEndCBox.setItems(FXCollections.observableArrayList(timeSlots));

        disableAndGreyOutTextField(updateAppIdTxt);
    }

    /**
     * Saves the updated appointment data after validation.
     * <p>
     * Validates the input based on several criteria:
     * - Checks if all required fields are filled.
     * - Ensures the end date/time is after the start date/time.
     * - Ensures the appointment doesn't exceed 8 hours.
     * - Ensures the appointment is scheduled during business hours (8:00 am to 5:00 pm).
     * - Checks if the entered contact name exists.
     * - Checks if the appointment time slot is free for the selected customer.
     * </p>
     * If all validations pass, it updates the appointment in the database.
     */
    @FXML
    private void saveUpdateAppointment() {
        // Required fields check
        if (updateAppTitleTxt.getText().trim().isEmpty() ||
                updateAppTypeTxt.getText().trim().isEmpty() ||
                updateAppDescriptionTxt.getText().trim().isEmpty() ||
                updateAppLocationTxt.getText().trim().isEmpty()) {
            Helper.showAlert("All fields are required.");
            return;
        }

        LocalDateTime startDateTime = getLocalDateTimeFromDatePickerAndComboBox(updateAppStartDPick, updateAppStartCBox);
        LocalDateTime endDateTime = getLocalDateTimeFromDatePickerAndComboBox(updateAppEndDPick, updateAppEndCBox);

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

        Appointment updatedAppointment = createUpdatedAppointment();

        if (appointmentDAO.isTimeSlotFree(updatedAppointment.getCustomerId(), updatedAppointment.getStart(), updatedAppointment.getEnd(), Integer.parseInt(updateAppIdTxt.getText()))) {
            appointmentDAO.updateAppointment(updatedAppointment);
            goToMainWindow();
        } else {
            Helper.showAlert(OVERLAPPING_SCHEDULE_MESSAGE);
        }
    }


    /**
     * Redirects the user to the main window after updating the appointment.
     */
    @FXML
    private void goToMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_WINDOW_FXML));
            Parent sceneRoot = loader.load();

            MainWindowController mainWindowController = loader.getController();
            mainWindowController.refreshAppointmentsTable();

            loadFXMLScene(sceneRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the form fields with the details of the appointment to be updated.
     *
     * @param appointment the Appointment object containing the details to populate the form.
     */
    public void initData(Appointment appointment) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN);

        // Set the values for the TextFields and ComboBoxes
        updateAppIdTxt.setText(String.valueOf(appointment.getId()));
        updateAppTitleTxt.setText(appointment.getTitle());
        updateAppDescriptionTxt.setText(appointment.getDescription());
        updateAppLocationTxt.setText(appointment.getLocation());
        updateAppTypeTxt.setText(appointment.getType());
        updateAppContactCBox.setValue(Helper.getContactName(appointment.getContactId()));
        updateAppCustIdCBox.setValue(appointment.getCustomerId());
        updateAppUserIdCBox.setValue(appointment.getUserId());
/*
        // Convert UTC time to local time before setting the combo box values
        updateAppStartCBox.setValue(timeFormatter.format(Helper.convertToLocal(appointment.getStart())));
        updateAppEndCBox.setValue(timeFormatter.format(Helper.convertToLocal(appointment.getEnd())));

        // Set the values for the DatePickers
        updateAppStartDPick.setValue(Helper.convertToLocal(appointment.getStart()).toLocalDate());
        updateAppEndDPick.setValue(Helper.convertToLocal(appointment.getEnd()).toLocalDate());

 */
        // Set the combo box values directly from the appointment's timestamps
        updateAppStartCBox.setValue(timeFormatter.format(appointment.getStart()));
        updateAppEndCBox.setValue(timeFormatter.format(appointment.getEnd()));

// Set the values for the DatePickers
        updateAppStartDPick.setValue(appointment.getStart().toLocalDate());
        updateAppEndDPick.setValue(appointment.getEnd().toLocalDate());

    }

    /**
     * Retrieves a sorted list of customer IDs.
     *
     * @return a List of sorted customer IDs.
     */
    private List<Integer> getSortedCustomerIds() {
        List<Integer> customerIds = Helper.getAllCustomerIds();
        Collections.sort(customerIds);
        return customerIds;
    }

    /**
     * Retrieves a sorted list of user IDs.
     *
     * @return a List of sorted user IDs.
     */
    private List<Integer> getSortedUserIds() {
        List<Integer> userIds = Helper.getAllUserIds();
        Collections.sort(userIds);
        return userIds;
    }

    /**
     * Generates a list of time slots based on a given start time, end time, and interval.
     *
     * @param start the start time.
     * @param end the end time.
     * @param intervalMinutes the interval in minutes between each time slot.
     * @return a List of time slots as strings.
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
     * Constructs an updated Appointment object using the form data.
     *
     * @return the updated Appointment object.
     */
    private Appointment createUpdatedAppointment() {
        int id = Integer.parseInt(updateAppIdTxt.getText());
        String title = updateAppTitleTxt.getText();
        String type = updateAppTypeTxt.getText();
        String description = updateAppDescriptionTxt.getText();
        String location = updateAppLocationTxt.getText();
        int contactId = Helper.getContactId(updateAppContactCBox.getValue());
        int customerId = updateAppCustIdCBox.getValue();
        int userId = updateAppUserIdCBox.getValue();

        LocalDateTime startDateTime = getLocalDateTimeFromDatePickerAndComboBox(updateAppStartDPick, updateAppStartCBox);
        LocalDateTime endDateTime = getLocalDateTimeFromDatePickerAndComboBox(updateAppEndDPick, updateAppEndCBox);

        return new Appointment(id, title, description, location, type, startDateTime, endDateTime, LocalDateTime.now(), "user", LocalDateTime.now(), "user", customerId, userId, contactId);
    }

    /**
     * Combines the selected date from a DatePicker and the selected time from a ComboBox
     * to create a LocalDateTime object.
     *
     * @param datePicker the DatePicker containing the selected date.
     * @param comboBox the ComboBox containing the selected time.
     * @return a LocalDateTime combining the selected date and time.
     */
    private LocalDateTime getLocalDateTimeFromDatePickerAndComboBox(DatePicker datePicker, ComboBox<String> comboBox) {
        LocalDate date = datePicker.getValue();
        LocalTime time = LocalTime.parse(comboBox.getValue(), DateTimeFormatter.ofPattern(TIME_PATTERN));
        return LocalDateTime.of(date, time);
    }

    /**
     * Disables the given TextField and sets its text color to grey.
     *
     * @param textField the TextField to be disabled and styled.
     */
    private void disableAndGreyOutTextField(TextField textField) {
        textField.setDisable(true);
        textField.setStyle("-fx-text-fill: grey;");
    }

    /**
     * Loads an FXML scene.
     *
     * @param sceneRoot the Parent object containing the FXML scene to load.
     */
    private void loadFXMLScene(Parent sceneRoot) {
        Scene scene = new Scene(sceneRoot);
        scene.getStylesheets().add(getClass().getResource(STYLESHEET_CSS).toExternalForm());

        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
