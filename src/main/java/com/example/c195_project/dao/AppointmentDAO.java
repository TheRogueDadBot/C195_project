package com.example.c195_project.dao;

import com.example.c195_project.helper.JDBC;
import com.example.c195_project.helper.Helper;
import com.example.c195_project.model.Appointment;
import com.example.c195_project.model.CustomerReport;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;

import static com.example.c195_project.helper.JDBC.connection;

/**
 * Data Access Object (DAO) class for handling CRUD operations related to appointments.
 * This class interacts directly with the database to fetch, create, update, and delete appointment records.
 */
public class AppointmentDAO {

    private static final String ALL_APPOINTMENTS_QUERY = "SELECT * FROM appointments";
    private static final String APPOINTMENT_BY_ID_QUERY = "SELECT * FROM appointments WHERE Appointment_ID = ?";
    private static final String ADD_APPOINTMENT_QUERY = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_APPOINTMENT_QUERY = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
    private static final String DELETE_APPOINTMENT_QUERY = "DELETE FROM appointments WHERE Appointment_ID = ?";
    private static final String MAX_APPOINTMENT_ID_QUERY = "SELECT MAX(Appointment_ID) AS max_id FROM appointments";
    private static final String APPOINTMENTS_BY_CUSTOMER_QUERY = "SELECT * FROM appointments WHERE Customer_ID = ?";
    private static final String ALL_CONTACT_NAMES_QUERY = "SELECT Contact_Name FROM contacts";

    private Connection conn;

    /**
     * Constructor for the AppointmentDAO. Initializes the database connection.
     */
    public AppointmentDAO() {
        this.conn = JDBC.getConnection();
    }

    /**
     * Fetches all appointments from the database.
     *
     * @return a list of all Appointment objects.
     */
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();

        try {
            PreparedStatement stmt = JDBC.getConnection().prepareStatement(ALL_APPOINTMENTS_QUERY);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Extract the appointment from the result set, this will have the start and end times in UTC
                Appointment appointment = extractAppointmentFromResultSet(rs);

                // Add the appointment object to the list
                appointments.add(appointment);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return appointments;
    }

    /**
     * Adds a new appointment to the database.
     *
     * @param appointment the Appointment object to be added.
     * @return true if the addition was successful, false otherwise.
     */
    public boolean addAppointment(Appointment appointment) {
        try {
          /*  // Convert the start and end times from local time to UTC before storing
            appointment.setStart(Helper.convertToUtc(appointment.getStart()));
            appointment.setEnd(Helper.convertToUtc(appointment.getEnd()));

           */

            PreparedStatement stmt = prepareStatementWithAppointmentData(JDBC.getConnection().prepareStatement(ADD_APPOINTMENT_QUERY), appointment);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Updates an existing appointment in the database.
     *
     * @param appointment the Appointment object with updated details.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateAppointment(Appointment appointment) {
        try {
           /* // Convert the start and end times from local time to UTC before storing
            appointment.setStart(Helper.convertToUtc(appointment.getStart()));
            appointment.setEnd(Helper.convertToUtc(appointment.getEnd()));

            */

            PreparedStatement stmt = prepareStatementWithAppointmentData(JDBC.getConnection().prepareStatement(UPDATE_APPOINTMENT_QUERY), appointment);
            stmt.setInt(14, appointment.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes an appointment from the database.
     *
     * @param id the ID of the appointment to be deleted.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteAppointment(int id) {
        try {
            PreparedStatement stmt = JDBC.getConnection().prepareStatement(DELETE_APPOINTMENT_QUERY);
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Gets the next available ID for creating a new appointment.
     *
     * @return the next available ID.
     */
    public int getNextAppointmentId() {
        int id = 0;
        try {
            PreparedStatement stmt = JDBC.getConnection().prepareStatement(MAX_APPOINTMENT_ID_QUERY);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("max_id") + 1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return id;
    }

    /**
     * Fetches all appointments associated with a specific customer.
     *
     * @param customerId the ID of the customer.
     * @return a list of all Appointment objects related to the given customer.
     */
    public List<Appointment> getAppointmentsByCustomer(int customerId) {
        List<Appointment> appointments = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(APPOINTMENTS_BY_CUSTOMER_QUERY);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                appointments.add(extractAppointmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    /**
     * Checks if a time slot is free for a specific customer.
     *
     * @param customerId          the ID of the customer.
     * @param start               the start time of the appointment.
     * @param end                 the end time of the appointment.
     * @param currentAppointmentId the ID of the current appointment (used when updating).
     * @return true if the time slot is free, false otherwise.
     */
    public boolean isTimeSlotFree(int customerId, LocalDateTime start, LocalDateTime end, Integer currentAppointmentId) {
        List<Appointment> appointments = getAppointmentsByCustomer(customerId);

        for (Appointment appointment : appointments) {
            // Skip the current appointment
            if (currentAppointmentId != null && appointment.getId() == currentAppointmentId) {
                continue;
            }

            // Check if the new appointment overlaps with the existing one
            if (start.isBefore(appointment.getEnd()) && end.isAfter(appointment.getStart())) {
                // If there's an overlap, return false
                return false;
            }
        }

        // If no overlaps were found, return true
        return true;
    }

    /**
     * Extracts appointment data from a given result set and creates an Appointment object.
     *
     * @param rs the ResultSet object containing the appointment data.
     * @return an Appointment object constructed using the data from the result set.
     * @throws SQLException if there's an error accessing the result set's data.
     */
    private Appointment extractAppointmentFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("Appointment_ID");
        String title = rs.getString("Title");
        String description = rs.getString("Description");
        String location = rs.getString("Location");
        String type = rs.getString("Type");
        LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
        LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
        LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
        String createdBy = rs.getString("Created_By");
        LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
        String lastUpdatedBy = rs.getString("Last_Updated_By");
        int customerId = rs.getInt("Customer_ID");
        int userId = rs.getInt("User_ID");
        int contactId = rs.getInt("Contact_ID");

        return new Appointment(id, title, description, location, type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, customerId, userId, contactId);
    }

    /**
     * Prepares a given PreparedStatement by setting its parameters with data from the provided Appointment object.
     * This method helps to avoid code duplication when preparing statements for inserting or updating appointments.
     *
     * @param stmt the PreparedStatement to be prepared.
     * @param appointment the Appointment object from which to extract the data.
     * @return the prepared PreparedStatement with data from the provided Appointment object.
     * @throws SQLException if there's an error setting the PreparedStatement's parameters.
     */
    private PreparedStatement prepareStatementWithAppointmentData(PreparedStatement stmt, Appointment appointment) throws SQLException {
        stmt.setString(1, appointment.getTitle());
        stmt.setString(2, appointment.getDescription());
        stmt.setString(3, appointment.getLocation());
        stmt.setString(4, appointment.getType());
        stmt.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));  // Removed Helper.convertToUtc
        stmt.setTimestamp(6, Timestamp.valueOf(appointment.getEnd()));  // Removed Helper.convertToUtc
        stmt.setTimestamp(7, Timestamp.valueOf(appointment.getCreateDate()));
        stmt.setString(8, appointment.getCreatedBy());
        stmt.setTimestamp(9, Timestamp.valueOf(appointment.getLastUpdate()));
        stmt.setString(10, appointment.getLastUpdatedBy());
        stmt.setInt(11, appointment.getCustomerId());
        stmt.setInt(12, appointment.getUserId());
        stmt.setInt(13, appointment.getContactId());

        return stmt;
    }

    /**
     * Generates a report of the number of appointments by type and month.
     *
     * @return a map where the key is the month and the value is another map with appointment types and their counts.
     */
    public Map<String, Map<String, Integer>> getAppointmentsCountByTypeAndMonth() {
        String sql = "SELECT MONTH(start) as Month, type, COUNT(*) as Count FROM appointments GROUP BY MONTH(start), type";

        Map<String, Map<String, Integer>> results = new HashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int monthNumber = rs.getInt("Month");
                String type = rs.getString("type");
                int count = rs.getInt("Count");

                // Convert the month number to a full name
                String monthName = Month.of(monthNumber).getDisplayName(TextStyle.FULL, Locale.getDefault());

                Map<String, Integer> monthData = results.getOrDefault(monthName, new HashMap<>());
                monthData.put(type, count);
                results.put(monthName, monthData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Retrieves the schedule of appointments for a specific contact.
     *
     * @param contactName the name of the contact.
     * @return a list of all Appointment objects for the given contact.
     */
    public List<Appointment> getScheduleByContact(String contactName) {
        String sql = "SELECT * FROM appointments WHERE Contact_ID = (SELECT Contact_ID FROM contacts WHERE Contact_Name = ?) ORDER BY start";
        List<Appointment> appointments = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, contactName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Appointment appointment = extractAppointmentFromResultSet(rs);
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    /**
     * Fetches the names of all contacts.
     *
     * @return a list of all contact names.
     */
    public List<String> getAllContactNames() {
        List<String> contactNames = new ArrayList<>();

        try {
            PreparedStatement stmt = JDBC.getConnection().prepareStatement(ALL_CONTACT_NAMES_QUERY);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String contactName = rs.getString("Contact_Name");
                contactNames.add(contactName);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return contactNames;
    }

    /**
     * Generates a report showing the frequency of appointments for each customer.
     *
     * @return a list of CustomerReport objects representing each customer and their appointment count.
     */
    public List<CustomerReport> getCustomerAppointmentFrequency() {
        List<CustomerReport> report = new ArrayList<>();
        String query = "SELECT c.Customer_Name as customerName, COUNT(a.Appointment_ID) as count\n" +
                "FROM appointments a\n" +
                "JOIN customers c ON a.Customer_ID = c.Customer_ID\n" +
                "GROUP BY c.Customer_Name\n";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String customerName = resultSet.getString("customerName");
                int count = resultSet.getInt("count");
                report.add(new CustomerReport(customerName, count));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return report;
    }
}