package com.example.c195_project.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.c195_project.model.User;

/**
 * Data Access Object (DAO) class for performing CRUD operations related to the User entity.
 */
public class UserDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    private static final String SELECT_USER_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_USER_QUERY = "INSERT INTO users (username, password, dateCreated, createdBy, lastUpdated, updatedBy) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET username = ?, password = ?, dateCreated = ?, createdBy = ?, lastUpdated = ?, updatedBy = ? WHERE id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";

    /**
     * Retrieves a user by its ID.
     *
     * @param id the ID of the user to retrieve.
     * @return the User object if found, otherwise null.
     */
    public User getUserById(int id) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_USER_BY_ID_QUERY);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Inserts a new user into the database.
     *
     * @param user the User object to add.
     * @return true if the insertion was successful, otherwise false.
     */
    public boolean createUser(User user) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = prepareStatementWithUserData(conn.prepareStatement(INSERT_USER_QUERY), user)) {

            return stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the details of a specific user in the database.
     *
     * @param user the User object containing the updated data.
     * @return true if the update was successful, otherwise false.
     */
    public boolean updateUser(User user) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = prepareStatementWithUserData(conn.prepareStatement(UPDATE_USER_QUERY), user)) {

            stmt.setInt(7, user.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a user by its ID.
     *
     * @param id the ID of the user to delete.
     * @return true if the deletion was successful, otherwise false.
     */
    public boolean deleteUser(int id) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(DELETE_USER_QUERY)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Converts a ResultSet row into a User object.
     *
     * @param rs the ResultSet containing user data.
     * @return a User object.
     * @throws SQLException if a database error occurs.
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("dateCreated"),
                rs.getString("createdBy"),
                rs.getString("lastUpdated"),
                rs.getString("updatedBy")
        );
    }

    /**
     * Prepares a SQL statement with data from a User object.
     *
     * @param stmt the prepared SQL statement.
     * @param user the User object containing the data.
     * @return a PreparedStatement populated with data from the user.
     * @throws SQLException if a database error occurs.
     */
    private PreparedStatement prepareStatementWithUserData(PreparedStatement stmt, User user) throws SQLException {
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        stmt.setString(3, user.getDateCreated());
        stmt.setString(4, user.getCreatedBy());
        stmt.setString(5, user.getLastUpdated());
        stmt.setString(6, user.getUpdatedBy());

        return stmt;
    }
}