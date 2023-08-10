package com.example.c195_project.dao;

import com.example.c195_project.model.Customer;
import com.example.c195_project.helper.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) class for performing CRUD operations related to the Customer entity.
 */
public class CustomerDAO {

    private static final String CUSTOMER_BY_ID_QUERY = "SELECT * FROM customers WHERE Customer_ID = ?";
    private static final String ALL_CUSTOMERS_QUERY = "SELECT * FROM customers";
    private static final String UPDATE_CUSTOMER_QUERY = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
    private static final String ADD_CUSTOMER_QUERY = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_APPOINTMENTS_QUERY = "DELETE FROM appointments WHERE Customer_ID = ?";
    private static final String DELETE_CUSTOMER_QUERY = "DELETE FROM customers WHERE Customer_ID = ?";

    private Connection conn;

    /**
     * Initializes the CustomerDAO with a database connection.
     */
    public CustomerDAO() {
        conn = JDBC.getConnection();
    }

    /**
     * Retrieves a customer by its ID.
     *
     * @param id the ID of the customer to retrieve.
     * @return the customer object if found, otherwise null.
     * @throws SQLException if a database error occurs.
     */
    public Customer getCustomer(int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(CUSTOMER_BY_ID_QUERY);
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return extractCustomerFromResultSet(rs);
        }

        return null;
    }

    /**
     * Retrieves all customers from the database.
     *
     * @return a list of all customers.
     * @throws SQLException if a database error occurs.
     */
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(ALL_CUSTOMERS_QUERY);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            customers.add(extractCustomerFromResultSet(rs));
        }

        return customers;
    }

    /**
     * Updates the details of a specific customer in the database.
     *
     * @param customer the customer object containing updated data.
     * @return true if the update was successful, otherwise false.
     * @throws SQLException if a database error occurs.
     */
    public boolean updateCustomer(Customer customer) throws SQLException {
        PreparedStatement stmt = prepareStatementWithCustomerData(conn.prepareStatement(UPDATE_CUSTOMER_QUERY), customer);
        stmt.setInt(6, customer.getId());

        int updated = stmt.executeUpdate();

        return updated > 0;
    }

    /**
     * Inserts a new customer into the database.
     *
     * @param customer the customer object to add.
     * @return true if the insertion was successful, otherwise false.
     * @throws SQLException if a database error occurs.
     */
    public boolean addCustomer(Customer customer) throws SQLException {
        PreparedStatement stmt = prepareStatementWithCustomerData(conn.prepareStatement(ADD_CUSTOMER_QUERY), customer);

        int inserted = stmt.executeUpdate();

        return inserted > 0;
    }

    /**
     * Deletes a customer by its ID. This method also deletes all appointments associated with the customer.
     *
     * @param customerId the ID of the customer to delete.
     */
    public void deleteCustomer(int customerId) {
        try {
            Connection connection = JDBC.getConnection();

            // First, delete all appointments for the customer
            PreparedStatement deleteAppointments = connection.prepareStatement(DELETE_APPOINTMENTS_QUERY);
            deleteAppointments.setInt(1, customerId);
            deleteAppointments.executeUpdate();

            // Then, delete the customer
            PreparedStatement deleteCustomer = connection.prepareStatement(DELETE_CUSTOMER_QUERY);
            deleteCustomer.setInt(1, customerId);
            deleteCustomer.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts a ResultSet row into a Customer object.
     *
     * @param rs the ResultSet containing customer data.
     * @return a Customer object.
     * @throws SQLException if a database error occurs.
     */
    private Customer extractCustomerFromResultSet(ResultSet rs) throws SQLException {
        return new Customer(
                rs.getInt("Customer_ID"),
                rs.getString("Customer_Name"),
                rs.getString("Address"),
                rs.getString("Postal_Code"),
                rs.getString("Phone"),
                rs.getString("Create_Date"),
                rs.getString("Created_By"),
                rs.getString("Last_Update"),
                rs.getString("Last_Updated_By"),
                rs.getInt("Division_ID")
        );
    }

    /**
     * Prepares a SQL statement with data from a Customer object.
     *
     * @param stmt the prepared SQL statement.
     * @param customer the customer object containing the data.
     * @return a PreparedStatement populated with data from the customer.
     * @throws SQLException if a database error occurs.
     */
    private PreparedStatement prepareStatementWithCustomerData(PreparedStatement stmt, Customer customer) throws SQLException {
        stmt.setString(1, customer.getName());
        stmt.setString(2, customer.getAddress());
        stmt.setString(3, customer.getPostalCode());
        stmt.setString(4, customer.getPhone());
        stmt.setInt(5, customer.getDivisionId());

        return stmt;
    }
}
