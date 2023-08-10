package com.example.c195_project.model;

/**
 * Represents a user of the application, capturing various attributes and metadata.
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String dateCreated;
    private String createdBy;
    private String lastUpdated;
    private String updatedBy;

    /**
     * Constructs a new User instance.
     *
     * @param id           The unique identifier of the user.
     * @param username     The username of the user.
     * @param password     The password of the user.
     * @param dateCreated  The creation date of the user's record.
     * @param createdBy    The name/identifier of the entity that created the user.
     * @param lastUpdated  The last update date of the user's record.
     * @param updatedBy    The name/identifier of the entity that last updated the user's record.
     */
    public User(int id, String username, String password, String dateCreated, String createdBy, String lastUpdated, String updatedBy) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.dateCreated = dateCreated;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.updatedBy = updatedBy;
    }

    // Getters

    /**
     * Gets the unique identifier of the user.
     *
     * @return The ID of the user.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the creation date of the user's record.
     *
     * @return The creation date.
     */
    public String getDateCreated() {
        return dateCreated;
    }

    /**
     * Gets the name or identifier of the entity that created the user.
     *
     * @return The creator's name or identifier.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Gets the last update date of the user's record.
     *
     * @return The last update date.
     */
    public String getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Gets the name or identifier of the entity that last updated the user's record.
     *
     * @return The last updater's name or identifier.
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    // Setters

    /**
     * Sets the creation date of the user's record.
     *
     * @param dateCreated The creation date to set.
     */
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Sets the name or identifier of the entity that created the user.
     *
     * @param createdBy The creator's name or identifier to set.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Sets the last update date of the user's record.
     *
     * @param lastUpdated The last update date to set.
     */
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * Sets the name or identifier of the entity that last updated the user's record.
     *
     * @param updatedBy The last updater's name or identifier to set.
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
