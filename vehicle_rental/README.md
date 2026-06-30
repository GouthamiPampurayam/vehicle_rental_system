# Vehicle Rental System

A professional desktop application built using Java Swing (manual UI, no builders) and JDBC (MySQL) following advanced Object-Oriented Programming (OOP) concepts.

## System Features

1. **Authentication**:
   - Secure login via database verification.
   - Default login details: Username `admin` | Password `admin`.
2. **Vehicle Management**:
   - Add, update, delete, view, and search vehicles.
   - Dynamic classification of vehicles into **Cars** and **Bikes** (polymorphic subclasses).
   - Validation for fields and non-negative daily rent amounts.
3. **Customer Management**:
   - Comprehensive customer registration (Name, Phone, License number).
   - Add, update, delete, view, and search customer database.
4. **Rental Transaction Management**:
   - Dynamically load available vehicles and registered customers.
   - Auto-calculate bills based on vehicle type pricing formulas (Car has a flat $15 markup; Bike calculates days x rent).
   - Perform Rent and Return operations within robust SQL transactions.
   - Automatic real-time status update for vehicles (Available `Yes` / `No`).
5. **Exception Handling**:
   - Intercept empty fields, negative inputs, duplicate values, and database connection failures, reporting them using friendly alert panels (`JOptionPane`).

---

## Technical Specifications

- **Language**: Java
- **Database**: MySQL
- **UI Framework**: Java Swing
- **ORM / Connector**: JDBC (`mysql-connector-j`)
- **Package Configuration**: Default (No packages used, per requirements)

---

## Database Schema Details

The application automatically verifies and initializes the database `vehicle_rental` and tables upon startup.

```sql
CREATE DATABASE IF NOT EXISTS vehicle_rental;
USE vehicle_rental;

-- Vehicle Table
CREATE TABLE IF NOT EXISTS vehicle (
    vehicle_id INT PRIMARY KEY AUTO_INCREMENT,
    vehicle_name VARCHAR(50),
    vehicle_type VARCHAR(20),
    rent_per_day DOUBLE,
    available VARCHAR(10)
);

-- Customer Table
CREATE TABLE IF NOT EXISTS customer (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(50),
    phone VARCHAR(15),
    license_number VARCHAR(30)
);

-- Rental Table
CREATE TABLE IF NOT EXISTS rental (
    rental_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    vehicle_id INT,
    rent_date DATE,
    return_date DATE,
    days INT,
    total_amount DOUBLE,
    status VARCHAR(20)
);

-- Login Table
CREATE TABLE IF NOT EXISTS login (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50)
);

-- Seed Account (inserted automatically if empty)
INSERT INTO login (username, password) VALUES ('admin', 'admin');
```

---

## Setup & Running Instructions

### Prerequisites
1. **Java Development Kit (JDK)**: Ensure JDK 8 or above is installed and configured in your `PATH`.
2. **MySQL Server**: Ensure MySQL is running on `localhost:3306` with username `root` and **no password** (empty). If a password is required, update `DBConnection.java`.
3. **MySQL JDBC Driver**: You must include the MySQL connector JAR file (e.g., `mysql-connector-j-8.x.x.jar`) in your Java compiler and runtime classpath.

### Compilation
Compile the project from your terminal inside the project directory:
```bash
javac -cp ".;path/to/mysql-connector-j.jar" *.java
```

### Execution
Run the compiled project:
```bash
java -cp ".;path/to/mysql-connector-j.jar" Main
```
