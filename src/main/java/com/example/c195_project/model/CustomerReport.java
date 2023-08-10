package com.example.c195_project.model;

/**
 * Represents a report for a customer, detailing the number of related items (like appointments).
 */
public class CustomerReport {
    private String customerName;
    private int count;  // Renamed from appointmentCount

    /**
     * Constructs a new CustomerReport instance.
     *
     * @param customerName The name of the customer.
     * @param count        The count of related items for the customer (e.g., appointments).
     */
    public CustomerReport(String customerName, int count) {
        this.customerName = customerName;
        this.count = count;  // Updated from appointmentCount
    }

    /**
     * Gets the name of the customer.
     *
     * @return The name of the customer.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Gets the count of related items for the customer (e.g., appointments).
     *
     * @return The count of related items.
     */
    public int getCount() {  // Renamed from getAppointmentCount
        return count;  // Updated from appointmentCount
    }
}
