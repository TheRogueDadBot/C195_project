package com.example.c195_project.model;

/**
 * Represents a customer in the system.
 */
public class Customer {
    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdatedBy;
    private int divisionId;
    private String state;
    private String country;

    /**
     * Constructs a new Customer instance.
     *
     * @param id            The unique identifier of the customer.
     * @param name          The name of the customer.
     * @param address       The address of the customer.
     * @param postalCode    The postal code of the customer.
     * @param phone         The phone number of the customer.
     * @param createDate    The date the customer was created.
     * @param createdBy     The user who created the customer.
     * @param lastUpdate    The date of the last update.
     * @param lastUpdatedBy The user who last updated the customer.
     * @param divisionId    The division ID associated with the customer.
     */
    public Customer(int id, String name, String address, String postalCode, String phone, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy, int divisionId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;
    }

    // Getters

    /**
     * @return The unique identifier of the customer.
     */
    public int getId() {
        return id;
    }

    /**
     * @return The name of the customer.
     */

    public String getName() {
        return name;
    }

    /**
     * @return The address of the customer.
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return The postal code of the customer.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @return The phone number of the customer.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @return The date the customer was created.
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * @return The user who created the customer.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @return The date of the last update for the customer.
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @return The user who last updated the customer.
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * @return The division ID associated with the customer.
     */
    public int getDivisionId() {
        return divisionId;
    }

    // Setters

    /**
     * Sets the unique identifier for the customer.
     *
     * @param id The unique identifier to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the name of the customer.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the address of the customer.
     *
     * @param address The address to set.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets the postal code for the customer.
     *
     * @param postalCode The postal code to set.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Sets the phone number for the customer.
     *
     * @param phone The phone number to set.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Sets the date the customer was created.
     *
     * @param createDate The creation date to set.
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * Sets the user who created the customer.
     *
     * @param createdBy The creator's name to set.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Sets the date of the last update for the customer.
     *
     * @param lastUpdate The last update date to set.
     */
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Sets the user who last updated the customer.
     *
     * @param lastUpdatedBy The last updater's name to set.
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Sets the division ID associated with the customer.
     *
     * @param divisionId The division ID to set.
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
}
