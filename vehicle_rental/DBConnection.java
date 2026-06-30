import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/vehicle_rental?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Empty password

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        String rootUrl = "jdbc:mysql://127.0.0.1:3306/?useSSL=false&allowPublicKeyRetrieval=true";
        String user = "root";
        String password = "";

        // First attempt to create database if it doesn't exist
        try (Connection conn = DriverManager.getConnection(rootUrl, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS vehicle_rental");
        } catch (SQLException e) {
            System.err.println("Could not verify/create database 'vehicle_rental': " + e.getMessage());
        }

        // Initialize tables inside the database
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS vehicle (" +
                    "vehicle_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "vehicle_name VARCHAR(50), " +
                    "vehicle_type VARCHAR(20), " +
                    "rent_per_day DOUBLE, " +
                    "available VARCHAR(10)" +
                    ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS customer (" +
                    "customer_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "customer_name VARCHAR(50), " +
                    "phone VARCHAR(15), " +
                    "license_number VARCHAR(30)" +
                    ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS rental (" +
                    "rental_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "customer_id INT, " +
                    "vehicle_id INT, " +
                    "rent_date DATE, " +
                    "return_date DATE, " +
                    "days INT, " +
                    "total_amount DOUBLE, " +
                    "status VARCHAR(20)" +
                    ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS login (" +
                    "username VARCHAR(50) PRIMARY KEY, " +
                    "password VARCHAR(50)" +
                    ")");

            // Ensure primary keys have AUTO_INCREMENT (in case tables were pre-existing without it)
            try {
                stmt.executeUpdate("ALTER TABLE vehicle MODIFY vehicle_id INT AUTO_INCREMENT");
                stmt.executeUpdate("ALTER TABLE customer MODIFY customer_id INT AUTO_INCREMENT");
                stmt.executeUpdate("ALTER TABLE rental MODIFY rental_id INT AUTO_INCREMENT");
            } catch (SQLException e) {
                // Ignore if constraint check or MySQL version prevents it, but try anyway
            }

            // Seed admin account
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM login")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    stmt.executeUpdate("INSERT INTO login (username, password) VALUES ('admin', 'admin')");
                }
            }

        } catch (SQLException e) {
            System.err.println("Could not initialize database tables: " + e.getMessage());
        }
    }
}
