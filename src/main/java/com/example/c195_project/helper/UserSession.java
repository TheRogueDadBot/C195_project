package com.example.c195_project.helper;

/**
 * Singleton class responsible for managing the user session.
 * This class provides functionality to store and retrieve the user's name for the current session.
 */
public class UserSession {
    private static UserSession session;

    private String userName;

    /**
     * Private constructor to ensure that the class cannot be instantiated from outside.
     *
     * @param userName The name of the user for the current session.
     */
    private UserSession(String userName) {
        this.userName = userName;
    }

    /**
     * Initializes the singleton instance of the UserSession class with the provided user name.
     * If the instance is already initialized, this method has no effect.
     *
     * @param userName The name of the user to initialize the session with.
     */
    public static void initInstance(String userName) {
        if (session == null) {
            session = new UserSession(userName);
        }
    }

    /**
     * Retrieves the singleton instance of the UserSession class.
     *
     * @return The singleton instance of the UserSession class.
     * @throws IllegalStateException if the instance has not been initialized using initInstance() method.
     */
    public static UserSession getInstance() {
        if (session == null) {
            throw new IllegalStateException("UserSession not initialized. Call initInstance() first.");
        }
        return session;
    }

    /**
     * Retrieves the user's name for the current session.
     *
     * @return The user's name for the current session.
     */
    public String getUserName() {
        return userName;
    }
}
