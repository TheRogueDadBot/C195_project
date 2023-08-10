package com.example.c195_project.model;

import com.example.c195_project.helper.Helper;
import java.time.LocalDateTime;

/**
 * Represents an appointment in the scheduling system.
 */
public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int customerId;
    private int userId;
    private int contactId;

    /**
     * Constructor to create an appointment instance with all attributes.
     *
     * @param id The unique identifier for the appointment.
     * @param title The title of the appointment.
     * @param description The detailed description of the appointment.
     * @param location The location where the appointment is scheduled.
     * @param type The type or category of the appointment.
     * @param start The start date and time of the appointment.
     * @param end The end date and time of the appointment.
     * @param createDate The date and time the appointment was created.
     * @param createdBy The user who created the appointment.
     * @param lastUpdate The date and time the appointment was last updated.
     * @param lastUpdatedBy The user who last updated the appointment.
     * @param customerId The unique identifier of the customer associated with the appointment.
     * @param userId The unique identifier of the user associated with the appointment.
     * @param contactId The unique identifier of the contact associated with the appointment.
     */
    public Appointment(int id, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int customerId, int userId, int contactId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    /**
     * Copy constructor to create a new Appointment object as a copy of another.
     *
     * @param other The Appointment object to copy.
     */
    public Appointment(Appointment other) {
        this.id = other.id;
        this.title = other.title;
        this.description = other.description;
        this.location = other.location;
        this.type = other.type;
        this.start = other.start;
        this.end = other.end;
        this.createDate = other.createDate;
        this.createdBy = other.createdBy;
        this.lastUpdate = other.lastUpdate;
        this.lastUpdatedBy = other.lastUpdatedBy;
        this.customerId = other.customerId;
        this.userId = other.userId;
        this.contactId = other.contactId;
    }

    // Getters
    /**
     * @return The unique identifier for the appointment.
     */
    public int getId() {
        return id;
    }

    /**
     * @return The title of the appointment.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The detailed description of the appointment.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return The location where the appointment is scheduled.
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return The type or category of the appointment.
     */
    public String getType() {
        return type;
    }

    /**
     * @return The start date and time of the appointment.
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * @return The end date and time of the appointment.
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * @return The date and time the appointment was created.
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * @return The user who created the appointment.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @return The date and time the appointment was last updated.
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @return The user who last updated the appointment.
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * @return The unique identifier of the customer associated with the appointment.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @return The unique identifier of the user associated with the appointment.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @return The unique identifier of the contact associated with the appointment.
     */
    public int getContactId() {
        return contactId;
    }

    // Setters
    /**
     * Sets the unique identifier for the appointment.
     *
     * @param id The unique identifier to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the title of the appointment.
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the detailed description of the appointment.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the location where the appointment is scheduled.
     *
     * @param location The location to set.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Sets the type or category of the appointment.
     *
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the start date and time of the appointment.
     *
     * @param start The start date and time to set.
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * Sets the end date and time of the appointment.
     *
     * @param end The end date and time to set.
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * Sets the date and time the appointment was created.
     *
     * @param createDate The creation date and time to set.
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * Sets the user who created the appointment.
     *
     * @param createdBy The creator's name to set.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Sets the date and time the appointment was last updated.
     *
     * @param lastUpdate The last update date and time to set.
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Sets the user who last updated the appointment.
     *
     * @param lastUpdatedBy The last updater's name to set.
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Sets the unique identifier of the customer associated with the appointment.
     *
     * @param customerId The customer's unique identifier to set.
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Sets the unique identifier of the user associated with the appointment.
     *
     * @param userId The user's unique identifier to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Sets the unique identifier of the contact associated with the appointment.
     *
     * @param contactId The contact's unique identifier to set.
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
}