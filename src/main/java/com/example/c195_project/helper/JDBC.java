package com.example.c195_project.helper;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Utility class responsible for managing the database connection to the client_schedule database.
 * Provides methods to open, retrieve, and close the connection.
 */
public class JDBC {

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String timeZoneSettings = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + timeZoneSettings; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    public static Connection connection;  // Connection Interface

    /**
     * Opens a connection to the client_schedule database.
     * If the connection is successful, a confirmation message is printed to the console.
     * If an error occurs, an error message is printed to the console.
     */
    public static void openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /**
     * Retrieves the active database connection object.
     *
     * @return the active {@link Connection} object, or null if no active connection exists.
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Closes the active connection to the client_schedule database.
     * If the connection is closed successfully, a confirmation message is printed to the console.
     * If an error occurs during the close operation, an error message is printed to the console.
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }
}
