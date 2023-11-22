GlobalConsultScheduler

Overview

GlobalConsultScheduler is a sophisticated, GUI-based scheduling desktop application designed for a global consulting organization. Developed with JavaFX and interfacing with a MySQL database, it streamlines scheduling across multiple countries and languages, adhering to specific business requirements and multinational operational needs.

Development Environment

IDE: IntelliJ Community 2022.2.3

JDK version: 17.0.6

JavaFX version: 20.0.1

MySQL Connector driver Version: mysql-connector-java-8.0.33

Features

Multilingual Support: Catering to users in Phoenix, White Plains, Montreal, and London.

Database Integration: Efficient MySQL database integration.

Scheduling: Manage appointments, meetings, and events across time zones and offices.

Read-Only Country and Division Data: Uses updated, third-party country and division information.

User Interface: Intuitive and user-friendly based on provided mock-ups.

Appointment Management: Create, update, delete, and view appointments to avoid scheduling overlaps.

Customer Management: Add, update, and delete customer records.

Reporting: Includes a report that counts the number of appointments for each customer.

Prerequisites

Java JDK 11 or higher.

JavaFX SDK.

MySQL Server and JDBC driver.

Setup and Installation

 Database Setup:

 Ensure MySQL Server is installed and running.

 Create a database named client_schedule.

 Use the provided SQL scripts to create and populate tables.

 Application Configuration:

 Update the JDBC URL, username, and password in JDBC.java to match your database credentials.

 Ensure the JavaFX SDK is configured in your project.

 Running the Application:

 Compile and run Main.java.

Database Connection

The application connects to a MySQL database using JDBC. The JDBC class in the helper package manages the database connection, opening, and closing operations. Update the database URL, username, and password in this class as per your MySQL setup.

User Session Management

UserSession is a singleton class responsible for managing the logged-in user's session. It stores the username and ensures that only one session is active at a time.

Contribution

Feel free to fork this repository and contribute to the project. Please follow the existing code style and naming conventions.

Usage

Launch the application from the Main class.

Log in using user information from the database.

Navigate between Appointments, Customers, and Reports tabs to manage data.

Log out via the logout button on the Appointments tab or close the application.

For detailed instructions, refer to UserManual.pdf in the documentation folder.

Additional Notes

The application ensures that scheduling overlaps do not occur.

It alerts the user of any upcoming appointments.

If there are no appointments for a customer, they will not appear in the reports.
