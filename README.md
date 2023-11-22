GlobalConsultScheduler

Overview

The GlobalConsultScheduler is a Java-based desktop application designed for managing client schedules. Developed with JavaFX, it provides a user-friendly interface for managing appointments, customers, and user sessions. The application connects to a MySQL database, enabling efficient data storage and retrieval.

Features

Appointment Management: Create, update, and delete appointments. View appointments by week, month, or all.

Customer Management: Add, edit, and delete customer records.

User Authentication: Handle user login and maintain user sessions.

Database Interaction: Perform CRUD operations on appointments and customers.

Localization: Support for different time zones and locales.

Reporting: Generate various reports like appointment types by month, schedules by contact, and customer appointment frequencies.

Prerequisites

Java JDK 11 or above.

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
