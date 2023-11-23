# Appointment Management System

## Overview
**GlobalConsultScheduler** is a GUI-based scheduling desktop application designed for global consulting organizations. It integrates JavaFX with MySQL to provide multilingual support and efficient scheduling across multiple countries.

## Development Environment
- **IDE**: IntelliJ Community 2022.2.3
- **JDK version**: 17.0.6
- **JavaFX version**: 20.0.1
- **MySQL Connector driver Version**: mysql-connector-java-8.0.33

## Features
- **Multilingual Support**: For Phoenix, White Plains, Montreal, and London.
- **Database Integration**: Efficient MySQL database integration.
- **Scheduling**: Manage appointments across time zones.
- **Country and Division Data**: Read-only, updated information.
- **User Interface**: Intuitive and user-friendly.
- **Appointment Management**: Create, update, and view appointments.
- **Customer Management**: Manage customer records.
- **Reporting**: Appointment count reports per customer.

## Prerequisites
- Java JDK 11 or higher
- JavaFX SDK
- MySQL Server and JDBC driver

## Setup and Installation
### Database Setup
- Ensure MySQL Server is running.
- Create `client_schedule` database.
- Use SQL scripts to populate tables.

### Application Configuration
- Update JDBC URL, username, and password in `JDBC.java`.
- Configure JavaFX SDK in the project.

### Running the Application
- Compile and run `Main.java`.

## Database Connection
Managed by the `JDBC` class in the helper package. Update the credentials as per your MySQL setup.

## User Session Management
`UserSession` manages the logged-in user's session, ensuring a single active session.

## Contribution
Contributions are welcome. Fork and follow existing code styles and conventions.

## Usage
- Launch from `Main` class.
- Log in with database user info.
- Navigate between Appointments, Customers, and Reports.
- Log out via the button on the Appointments tab.

## Additional Notes
- Prevents scheduling overlaps.
- Alerts for upcoming appointments.
- Excludes customers with no appointments from reports.

For detailed instructions, see `UserManual.pdf` in the documentation folder.
