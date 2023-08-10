package com.example.c195_project.controller;

import com.example.c195_project.dao.AppointmentDAO;
import com.example.c195_project.dao.CustomerDAO;
import com.example.c195_project.helper.Helper;
import com.example.c195_project.model.Appointment;
import com.example.c195_project.model.Customer;
import com.example.c195_project.model.CustomerReport;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;
/**
 * The MainWindowController class manages the primary user interface of the application.
 * This includes functionalities related to managing appointments and customers, as well
 * as generating various reports. The class interacts with data access objects to
 * perform CRUD operations and updates the GUI accordingly.
 */
public class MainWindowController {

    private static final Logger LOGGER = Logger.getLogger(MainWindowController.class.getName());
    // FXML-annotated fields for GUI components
    @FXML
    private Tab reportsTab;

    @FXML
    private TableView<Map.Entry<String, Map.Entry<String, Integer>>> reportsTableView;

    @FXML
    private TableColumn<Map.Entry<String, Map.Entry<String, Integer>>, String> monthColumn;

    @FXML
    private TableColumn<Map.Entry<String, Map.Entry<String, Integer>>, String> typeColumn;

    @FXML
    private TableColumn<Map.Entry<String, Map.Entry<String, Integer>>, Integer> countColumn;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private TabPane mainTabPane;
    @FXML
    private Tab appointmentsTab;
    @FXML
    private Tab customersTab;
    @FXML
    private Button deleteAppointmentBtn;
    @FXML
    private TableView<Appointment> appointmentsTable;
    @FXML
    private TableColumn<Appointment, Integer> apptId;
    @FXML
    private TableColumn<Appointment, String> title;
    @FXML
    private TableColumn<Appointment, String> description;
    @FXML
    private TableColumn<Appointment, String> locationColumn;
    @FXML
    private TableColumn<Appointment, Integer> contact;
    @FXML
    private TableColumn<Appointment, String> type;
    @FXML
    private TableColumn<Appointment, LocalDateTime> start;
    @FXML
    private TableColumn<Appointment, LocalDateTime> end;
    @FXML
    private TableColumn<Appointment, Integer> customerId;
    @FXML
    private TableColumn<Appointment, Integer> userId;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> idColumn;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> addressColumn;
    @FXML
    private TableColumn<Customer, String> postalCodeColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TableColumn<Customer, String> createdDateColumn;
    @FXML
    private TableColumn<Customer, String> createdByColumn;
    @FXML
    private TableColumn<Customer, String> lastUpdateColumn;
    @FXML
    private TableColumn<Customer, String> lastUpdatedByColumn;
    @FXML
    private TableColumn<Customer, String> stateColumn;
    @FXML
    private TableColumn<Customer, String> countryColumn;
    @FXML
    private Button updateAppointmentBtn;
    @FXML
    private RadioButton weeklyRadio;

    @FXML
    private RadioButton monthlyRadio;

    @FXML
    private RadioButton allRadio;

    @FXML
    private TableView<?> report1TableView;
    @FXML
    private TableView<Appointment> report2TableView;

    @FXML
    private TableView<CustomerReport> report3TableView;

    @FXML
    private TableColumn<CustomerReport, String> customerNameColumn;
    @FXML
    private TableColumn<CustomerReport, Integer> appointmentCountColumn;

    @FXML
    private TableColumn<Appointment, Integer> r2AppointmentIdColumn;
    @FXML
    private TableColumn<Appointment, String> r2TitleColumn;
    @FXML
    private TableColumn<Appointment, String> r2TypeColumn;
    @FXML
    private TableColumn<Appointment, String> r2DescriptionColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> r2StartColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> r2EndColumn;
    @FXML
    private TableColumn<Appointment, Integer> r2CustomerIdColumn;
    @FXML
    private ComboBox<String> reportContactCBox;


    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private CustomerDAO customerDAO = new CustomerDAO();

    /**
     * Initializes the controller after the root element has been completely processed.
     * Sets up cell factories for table columns, configures button actions, and
     * initializes data in the tables. Lambda expressions are used in cell factories for
     * efficient and concise implementation without the need for additional classes or methods.
     */
    public void initialize() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        start.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : formatter.format(item));
            }
        });

        end.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : formatter.format(item));
            }
        });

        apptId.setCellValueFactory(new PropertyValueFactory<>("id"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        start.setCellValueFactory(new PropertyValueFactory<>("start"));
        end.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userId.setCellValueFactory(new PropertyValueFactory<>("userId"));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        createdDateColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        createdByColumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        lastUpdateColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        lastUpdatedByColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("divisionId"));

        refreshAppointmentsTable();

        updateAppointmentBtn.setOnAction(e -> updateAppointment());
        loadAppointments();
        loadCustomers();
        deleteAppointmentBtn.setOnAction(e -> deleteAppointment());
        weeklyRadio.setOnAction(e -> filterAppointmentsByWeek());
        monthlyRadio.setOnAction(e -> filterAppointmentsByMonth());
        allRadio.setOnAction(e -> loadAppointments());

        generateAppointmentReport();
        generateContactScheduleReport();

        r2AppointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        r2TitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        r2TypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        r2DescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        r2StartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        r2StartColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : formatter.format(item));
            }
        });

        r2EndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        r2EndColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : formatter.format(item));
            }
        });

        r2CustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        List<String> contactNames = appointmentDAO.getAllContactNames();
        reportContactCBox.getItems().addAll(contactNames);

        reportContactCBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            List<Appointment> appointments = appointmentDAO.getScheduleByContact(newValue);
            report2TableView.setItems(FXCollections.observableArrayList(appointments));
        });

        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        appointmentCountColumn.setCellValueFactory(new PropertyValueFactory<>("count"));

        populateCustomerAppointmentFrequencyTable();
    }

    /**
     * Generates a report showing the number of appointment types by month.
     * The report is represented as a table where each row shows the month, type, and count.
     */
    private void generateAppointmentReport() {
        // Get the data from the DAO
        Map<String, Map<String, Integer>> reportData = appointmentDAO.getAppointmentsCountByTypeAndMonth();

        // Flatten the nested map into a list of entries
        List<Map.Entry<String, Map.Entry<String, Integer>>> flattenedData = new ArrayList<>();
        for (Map.Entry<String, Map<String, Integer>> monthEntry : reportData.entrySet()) {
            for (Map.Entry<String, Integer> typeEntry : monthEntry.getValue().entrySet()) {
                flattenedData.add(new AbstractMap.SimpleEntry<>(monthEntry.getKey(), typeEntry));
            }
        }

        // Bind the table columns to the properties of the map entries
        monthColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey()));
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getValue().getKey()));
        countColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getValue().getValue()).asObject());

        // Add the data to the table
        reportsTableView.getItems().setAll(flattenedData);
    }

    /**
     * Generates a report displaying the schedule of contacts.
     * Populates the TableView with appointments for the selected contact.
     */
    public void generateContactScheduleReport() {
        // Initially populate the TableView with the appointments for the first contact in the ComboBox
        if (!reportContactCBox.getItems().isEmpty()) {
            String firstContactName = reportContactCBox.getItems().get(0);
            List<Appointment> appointments = appointmentDAO.getScheduleByContact(firstContactName);
            report2TableView.setItems(FXCollections.observableArrayList(appointments));
        }
    }

    /**
     * Loads all appointments from the database and displays them in the appointments table.
     */
    private void loadAppointments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList(appointmentDAO.getAllAppointments());

        apptId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        title.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getTitle()));
        description.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDescription()));
        locationColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getLocation()));
        contact.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getContactId()));
        type.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getType()));
        start.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getStart()));
        end.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getEnd()));
        customerId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCustomerId()));
        userId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getUserId()));

        appointmentsTable.setItems(appointments);
    }

    /**
     * Loads all customers from the database and displays them in the customer table.
     */
    private void loadCustomers() {
        try {
            ObservableList<Customer> customerList = FXCollections.observableArrayList(customerDAO.getAllCustomers());

            idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
            nameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));
            addressColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAddress()));
            postalCodeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPostalCode()));
            phoneColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPhone()));
            createdDateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCreateDate()));
            createdByColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCreatedBy()));
            lastUpdateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getLastUpdate()));
            lastUpdatedByColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getLastUpdatedBy()));
            stateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(Helper.getDivisionData(cellData.getValue().getDivisionId())));
            countryColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(Helper.getCountryData(cellData.getValue().getDivisionId())));

            customerTable.setItems(customerList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the "Add Appointment" window when the corresponding button is clicked.
     * @param event the action event triggering this method.
     */
    @FXML
    private void addAppointment(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/c195_project/AddAppointment.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the selected appointment details.
     * This method fetches the selected appointment from the table, then loads the
     * UpdateAppointment view and initializes it with the appointment details.
     */
    @FXML
    private void updateAppointment() {
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("Please select an appointment to update.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/c195_project/UpdateAppointment.fxml"));
            Parent updateAppointmentWindow = loader.load();
            UpdateAppointmentController controller = loader.getController();

            // Create a copy of the selected appointment and pass it to the Update Appointment window
            Appointment appointmentCopy = new Appointment(
                    selectedAppointment.getId(),
                    selectedAppointment.getTitle(),
                    selectedAppointment.getDescription(),
                    selectedAppointment.getLocation(),
                    selectedAppointment.getType(),
                    selectedAppointment.getStart(),
                    selectedAppointment.getEnd(),
                    selectedAppointment.getCreateDate(),
                    selectedAppointment.getCreatedBy(),
                    selectedAppointment.getLastUpdate(),
                    selectedAppointment.getLastUpdatedBy(),
                    selectedAppointment.getCustomerId(),
                    selectedAppointment.getUserId(),
                    selectedAppointment.getContactId()
            );
            controller.initData(appointmentCopy);

            // Get the stage from any component. In this case, we get it from the appointmentsTable.
            Stage stage = (Stage) appointmentsTable.getScene().getWindow();
            Scene scene = new Scene(updateAppointmentWindow);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a new window to add a customer.
     * This method loads the AddCustomer view when the "Add Customer" button is clicked.
     * @param event the action event triggering this method.
     */
    @FXML
    private void addCustomer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/c195_project/AddCustomer.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the selected customer details.
     * This method fetches the selected customer from the table, then loads the
     * UpdateCustomer view and initializes it with the customer details.
     * @param event the action event triggering this method.
     */
    @FXML
    private void updateCustomer(ActionEvent event) {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            Helper.showAlert("No customer selected for updating");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/c195_project/UpdateCustomer.fxml"));
            Parent root = loader.load();
            UpdateCustomerController controller = loader.getController();
            controller.setCustomer(selectedCustomer);

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the selected customer.
     * The method prompts the user for confirmation before deleting the customer and
     * its associated appointments. Once confirmed, the customer is removed from the database.
     * @param event the action event triggering this method.
     */
    @FXML
    public void deleteCustomer(ActionEvent event) {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            Helper.showAlert("No customer selected for deletion");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer and all associated appointments?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            customerDAO.deleteCustomer(selectedCustomer.getId());
            loadCustomers();  // reload the customers to reflect the deletion in the TableView
        }
    }

    /**
     * Deletes the selected appointment.
     * The method prompts the user for confirmation before deleting the appointment.
     * Once confirmed, the appointment is removed from the database and the table is updated.
     */
    @FXML
    private void deleteAppointment() {
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("Please select an appointment to delete.");
            alert.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this appointment?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            // Delete the appointment from the database
            appointmentDAO.deleteAppointment(selectedAppointment.getId());

            // Remove the appointment from the table view
            appointmentsTable.getItems().remove(selectedAppointment);

            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Information");
            info.setHeaderText("Appointment Deleted");
            info.setContentText(String.format("Appointment # %d of type %s has been deleted.",
                    selectedAppointment.getId(), selectedAppointment.getType()));
            info.showAndWait();

            // Refresh the appointments table
            refreshAppointmentsTable();
        }
    }

    /**
     * Logs out the user and redirects them to the login window.
     * @param event the action event triggering this method.
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        System.out.println("User has logged out");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/c195_project/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exits the application by closing the main window.
     */
    @FXML
    private void handleExit() {
        Stage stage = (Stage) mainBorderPane.getScene().getWindow();
        stage.close();
    }

    /**
     * Refreshes the appointments table with the latest data from the database.
     */
    void refreshAppointmentsTable() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList(appointmentDAO.getAllAppointments());
        appointmentsTable.setItems(appointments);
        appointmentsTable.refresh(); // Explicitly refresh the table view
    }

    /**
     * Filters the appointments table to show only appointments scheduled for the current week.
     */
    private void filterAppointmentsByWeek() {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList(appointmentDAO.getAllAppointments());
        ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

        for (Appointment appointment : allAppointments) {
            // Check if the appointment is within the current week
            if (isWithinThisWeek(appointment.getStart().toLocalDate()) && isWithinThisWeek(appointment.getEnd().toLocalDate())) {
                filteredAppointments.add(appointment);
            }
        }

        appointmentsTable.setItems(filteredAppointments);
    }

    /**
     * Filters the appointments table to show only appointments scheduled for the current month.
     */
    private void filterAppointmentsByMonth() {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList(appointmentDAO.getAllAppointments());
        ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

        for (Appointment appointment : allAppointments) {
            // Check if the appointment is within the current month
            if (isWithinThisMonth(appointment.getStart().toLocalDate()) && isWithinThisMonth(appointment.getEnd().toLocalDate())) {
                filteredAppointments.add(appointment);
            }
        }

        appointmentsTable.setItems(filteredAppointments);
    }

    /**
     * Checks if a given date is within the current week.
     * @param date the date to be checked.
     * @return true if the date is within the current week, false otherwise.
     */
    private boolean isWithinThisWeek(LocalDate date) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        return date.isEqual(startOfWeek) || date.isEqual(endOfWeek) || (date.isAfter(startOfWeek) && date.isBefore(endOfWeek));
    }

    /**
     * Checks if a given date is within the current month.
     * @param date the date to be checked.
     * @return true if the date is within the current month, false otherwise.
     */
    private boolean isWithinThisMonth(LocalDate date) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return date.isEqual(startOfMonth) || date.isEqual(endOfMonth) || (date.isAfter(startOfMonth) && date.isBefore(endOfMonth));
    }

    /**
     * Populates the customer appointment frequency table with data.
     * The table displays the frequency of appointments for each customer.
     */
    private void populateCustomerAppointmentFrequencyTable() {
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        List<CustomerReport> reportData = appointmentDAO.getCustomerAppointmentFrequency();
        report3TableView.setItems(FXCollections.observableArrayList(reportData));
    }

}