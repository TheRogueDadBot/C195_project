package com.example.c195_project.helper;

import javafx.scene.control.Alert;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.c195_project.helper.JDBC;

/**
 * Utility class that provides various helper methods for database operations and data conversion.
 */
public class Helper {
    private static final String INFO_DIALOG_TITLE = "Information Dialog";
    private static final String SELECT_CONTACT_ID_QUERY = "SELECT Contact_ID FROM contacts WHERE Contact_Name = ?";
    private static final String SELECT_CONTACT_NAME_QUERY = "SELECT Contact_Name FROM contacts WHERE Contact_ID = ?";

    private static final String SELECT_USERNAME_QUERY = "SELECT User_Name FROM users WHERE User_ID = ?";
    private static final String SELECT_COUNTRY_DATA_QUERY = "SELECT countries.Country FROM countries " +
            "JOIN first_level_divisions ON countries.Country_ID = first_level_divisions.Country_ID " +
            "WHERE first_level_divisions.Division_ID = ?";
    private static final String SELECT_DIVISIONS_BY_COUNTRY_QUERY = "SELECT Division FROM first_level_divisions WHERE Country_ID = ?";
    private static final String SELECT_MAX_CUSTOMER_ID_QUERY = "SELECT MAX(Customer_ID) AS MaxID FROM customers";
    private static final String INSERT_CUSTOMER_QUERY = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Country_ID, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_COUNTRIES_QUERY = "SELECT Country FROM countries";
    private static final String SELECT_ALL_DIVISIONS_QUERY = "SELECT Division FROM first_level_divisions";
    private static final String UPDATE_CUSTOMER_QUERY = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
    private static final String SELECT_ALL_CONTACT_NAMES_QUERY = "SELECT Contact_Name FROM contacts";
    private static final String SELECT_ALL_CUSTOMER_IDS_QUERY = "SELECT Customer_ID FROM customers";
    private static final String SELECT_ALL_USER_IDS_QUERY = "SELECT User_ID FROM users";
    private static final String SELECT_COUNTRY_ID_BY_NAME_QUERY = "SELECT Country_ID FROM countries WHERE Country = ?";
    private static final String SELECT_DIVISION_ID_BY_COUNTRY_AND_DIVISION_NAMES_QUERY =
            "SELECT first_level_divisions.Division_ID FROM first_level_divisions " +
                    "JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID " +
                    "WHERE countries.Country = ? AND first_level_divisions.Division = ?";
    private static final String SELECT_DIVISION_BY_ID_QUERY = "SELECT Division FROM first_level_divisions WHERE Division_ID = ?";



    /**
     * Displays an informational alert with a specified message.
     *
     * @param message the message to display in the alert.
     */
    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(INFO_DIALOG_TITLE);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Retrieves the ID of a contact based on its name.
     *
     * @param contactName the name of the contact.
     * @return the ID of the contact, or -1 if not found.
     */
    public static int getContactId(String contactName) {
        return getIdByName(SELECT_CONTACT_ID_QUERY, contactName);
    }

    /**
     * Retrieves the name of a contact based on its ID.
     *
     * @param contactId the ID of the contact.
     * @return the name of the contact, or null if not found.
     */
    public static String getContactName(int contactId) {
        return getNameById(SELECT_CONTACT_NAME_QUERY, contactId);
    }

    public static int getUserId(String userName) {
        return getIdByName(SELECT_USERNAME_QUERY, userName);
    }

    public static String getUsername(int userId) {
        return getNameById(SELECT_USERNAME_QUERY, userId);
    }

    public static int getDivisionId(String countryName, String divisionName) {
        int divisionId = -1;

        try {
            PreparedStatement statement = JDBC.getConnection().prepareStatement(SELECT_DIVISION_ID_BY_COUNTRY_AND_DIVISION_NAMES_QUERY);
            statement.setString(1, countryName);
            statement.setString(2, divisionName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                divisionId = resultSet.getInt("Division_ID");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return divisionId;
    }

    public static String getCountryData(int divisionId) {
        return getNameById(SELECT_COUNTRY_DATA_QUERY, divisionId);
    }

    public static String getDivisionData(int divisionId) {
        return getNameById(SELECT_DIVISION_BY_ID_QUERY, divisionId);
    }


    /**
     * Converts a LocalDateTime to its UTC equivalent.
     *
     * @param localDateTime the local date-time to convert.
     * @return the equivalent UTC date-time.
     */
    public static LocalDateTime convertToUtc(LocalDateTime localDateTime) {
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime utc = zdt.withZoneSameInstant(ZoneId.of("UTC"));
        return utc.toLocalDateTime();
    }

    /**
     * Converts a UTC LocalDateTime to its local equivalent.
     *
     * @param utcDateTime the UTC date-time to convert.
     * @return the equivalent local date-time.
     */
    public static LocalDateTime convertToLocal(LocalDateTime utcDateTime) {
        ZonedDateTime zdt = utcDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime local = zdt.withZoneSameInstant(ZoneId.systemDefault());
        return local.toLocalDateTime();
    }

    /**
     * Converts a LocalDateTime to Eastern Time.
     *
     * @param localDateTime the local date-time to convert.
     * @return the date-time in Eastern Time.
     */
    public static LocalDateTime convertToEastern(LocalDateTime localDateTime) {
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime eastern = zdt.withZoneSameInstant(ZoneId.of("America/New_York"));
        return eastern.toLocalDateTime();
    }

    /**
     * Retrieves the ID of the country based on its name.
     *
     * @param countryName the name of the country for which the ID is to be retrieved.
     * @return the ID of the specified country; returns -1 if the country is not found or an error occurs.
     */
    public static int getCountryId(String countryName) {
        return getIdByName(SELECT_COUNTRY_ID_BY_NAME_QUERY, countryName);
    }

    /**
     * Retrieves the name of the country based on its ID.
     *
     * @param countryId the ID of the country for which the name is to be retrieved.
     * @return the name of the specified country; returns null if the country is not found or an error occurs.
     */
    public static String getCountryName(int countryId) {
        return getNameById(SELECT_COUNTRY_DATA_QUERY, countryId);
    }

    /**
     * Retrieves a list of divisions associated with a given country name.
     *
     * @param countryName the name of the country for which divisions are to be retrieved.
     * @return a list of divisions associated with the specified country;
     *         returns an empty list if no divisions are found or an error occurs.
     */
    public static List<String> getDivisions(String countryName) {
        List<String> divisions = new ArrayList<>();

        try {
            PreparedStatement statement = JDBC.getConnection().prepareStatement(SELECT_DIVISIONS_BY_COUNTRY_QUERY);
            statement.setInt(1, getCountryId(countryName));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                divisions.add(resultSet.getString("Division"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return divisions;
    }


    /**
     * Generates the next customer ID by retrieving the maximum current customer ID and adding one.
     *
     * @return the next customer ID.
     */
    public static int getNextCustomerId() {
        return getMaxId(SELECT_MAX_CUSTOMER_ID_QUERY) + 1;
    }

    /**
     * Inserts a new customer into the database.
     *
     * @param id the ID of the customer.
     * @param name the name of the customer.
     * @param address the address of the customer.
     * @param postalCode the postal code of the customer.
     * @param phone the phone number of the customer.
     * @param countryId the country ID of the customer's address.
     * @param divisionId the division ID of the customer's address.
     * @return true if the insertion is successful, false otherwise.
     */
    public static boolean insertCustomer(int id, String name, String address, String postalCode, String phone, int countryId, int divisionId) {
        try {
            PreparedStatement statement = JDBC.getConnection().prepareStatement(INSERT_CUSTOMER_QUERY);
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setString(3, address);
            statement.setString(4, postalCode);
            statement.setString(5, phone);
            statement.setInt(6, countryId);
            statement.setInt(7, divisionId);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a list of all country names.
     *
     * @return a list containing the names of all countries.
     */
    public static List<String> getAllCountryNames() {
        return getAllNames(SELECT_ALL_COUNTRIES_QUERY);
    }

    /**
     * Retrieves a list of all division names.
     *
     * @return a list containing the names of all divisions.
     */
    public static List<String> getAllDivisionNames() {
        return getAllNames(SELECT_ALL_DIVISIONS_QUERY);
    }

    /**
     * Updates a customer's details in the database.
     *
     * @param id the ID of the customer to update.
     * @param name the new name of the customer.
     * @param address the new address of the customer.
     * @param postalCode the new postal code of the customer.
     * @param phone the new phone number of the customer.
     * @param divisionId the new division ID of the customer's address.
     * @return true if the update is successful, false otherwise.
     */
    public static boolean updateCustomer(int id, String name, String address, String postalCode, String phone, int divisionId) {
        try {
            PreparedStatement statement = JDBC.getConnection().prepareStatement(UPDATE_CUSTOMER_QUERY);
            statement.setString(1, name);
            statement.setString(2, address);
            statement.setString(3, postalCode);
            statement.setString(4, phone);
            statement.setInt(5, divisionId);
            statement.setInt(6, id);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a list of all contact names.
     *
     * @return a list containing the names of all contacts.
     */
    public static List<String> getAllContactNames() {
        return getAllNames(SELECT_ALL_CONTACT_NAMES_QUERY);
    }

    /**
     * Retrieves a list of all customer IDs.
     *
     * @return a list containing the IDs of all customers.
     */
    public static List<Integer> getAllCustomerIds() {
        return getAllIds(SELECT_ALL_CUSTOMER_IDS_QUERY);
    }

    /**
     * Retrieves a list of all user IDs.
     *
     * @return a list containing the IDs of all users.
     */
    public static List<Integer> getAllUserIds() {
        return getAllIds(SELECT_ALL_USER_IDS_QUERY);
    }

    /**
     * Retrieves the system's default ZoneId.
     *
     * @return the default ZoneId of the system.
     */
    public static ZoneId getZoneId() {
        return ZoneId.systemDefault();
    }

    /**
     * Retrieves the ID associated with a given name from the database.
     *
     * @param query the SQL query to execute.
     * @param name the name to use in the query.
     * @return the ID associated with the name, or -1 if not found.
     */
    private static int getIdByName(String query, String name) {
        int id = -1;
        try {
            PreparedStatement statement = JDBC.getConnection().prepareStatement(query);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            System.out.println("Name: " + name + ", ID: " + id);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return id;
    }

    /**
     * Retrieves the name associated with a given ID from the database.
     *
     * @param query the SQL query to execute.
     * @param id the ID to use in the query.
     * @return the name associated with the ID, or null if not found.
     */
    private static String getNameById(String query, int id) {
        String name = null;
        try {
            PreparedStatement statement = JDBC.getConnection().prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                name = resultSet.getString(1);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return name;
    }

    /**
     * Retrieves the maximum ID from the table specified in the query.
     *
     * @param query the SQL query string to get the maximum ID.
     * @return the maximum ID found; returns 0 if no ID is found or an error occurs.
     */
    private static int getMaxId(String query) {
        int maxId = 0;
        try {
            PreparedStatement statement = JDBC.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                maxId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return maxId;
    }

    /**
     * Retrieves a list of names from the table specified in the query.
     *
     * @param query the SQL query string to get all names.
     * @return a list of names; returns an empty list if no names are found or an error occurs.
     */
    private static List<String> getAllNames(String query) {
        List<String> names = new ArrayList<>();
        try {
            PreparedStatement stmt = JDBC.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                names.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return names;
    }

    /**
     * Retrieves a list of IDs from the table specified in the query.
     *
     * @param query the SQL query string to get all IDs.
     * @return a list of IDs; returns an empty list if no IDs are found or an error occurs.
     */
    private static List<Integer> getAllIds(String query) {
        List<Integer> ids = new ArrayList<>();
        try {
            PreparedStatement stmt = JDBC.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ids.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ids;
    }
}