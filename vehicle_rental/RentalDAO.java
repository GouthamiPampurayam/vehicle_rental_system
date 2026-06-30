import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalDAO {

    public boolean rentVehicle(Rental rental) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            String sql = "INSERT INTO rental (customer_id, vehicle_id, rent_date, return_date, days, total_amount, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, rental.getCustomerId());
            pstmt.setInt(2, rental.getVehicleId());
            pstmt.setDate(3, rental.getRentDate());
            pstmt.setDate(4, rental.getReturnDate());
            pstmt.setInt(5, rental.getDays());
            pstmt.setDouble(6, rental.getTotalAmount());
            pstmt.setString(7, "Rented");

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating rental record failed.");
            }

            // Get generated key if needed
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    rental.setRentalId(generatedKeys.getInt(1));
                }
            }

            // Update vehicle availability to "No"
            String updateVehicleSql = "UPDATE vehicle SET available = 'No' WHERE vehicle_id = ?";
            try (PreparedStatement updateVehiclePstmt = conn.prepareStatement(updateVehicleSql)) {
                updateVehiclePstmt.setInt(1, rental.getVehicleId());
                updateVehiclePstmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    // Ignore rollback exception
                }
            }
            throw e;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public boolean returnVehicle(int rentalId, Date returnDate) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            // 1. Get rental details to find vehicle ID
            int vehicleId = -1;
            String selectSql = "SELECT vehicle_id FROM rental WHERE rental_id = ?";
            try (PreparedStatement selectPstmt = conn.prepareStatement(selectSql)) {
                selectPstmt.setInt(1, rentalId);
                try (ResultSet rs = selectPstmt.executeQuery()) {
                    if (rs.next()) {
                        vehicleId = rs.getInt("vehicle_id");
                    }
                }
            }

            if (vehicleId == -1) {
                throw new SQLException("Rental record with ID " + rentalId + " not found.");
            }

            // 2. Update rental status and return date
            String updateRentalSql = "UPDATE rental SET return_date = ?, status = 'Returned' WHERE rental_id = ?";
            try (PreparedStatement updateRentalPstmt = conn.prepareStatement(updateRentalSql)) {
                updateRentalPstmt.setDate(1, returnDate);
                updateRentalPstmt.setInt(2, rentalId);
                updateRentalPstmt.executeUpdate();
            }

            // 3. Update vehicle availability to "Yes"
            String updateVehicleSql = "UPDATE vehicle SET available = 'Yes' WHERE vehicle_id = ?";
            try (PreparedStatement updateVehiclePstmt = conn.prepareStatement(updateVehicleSql)) {
                updateVehiclePstmt.setInt(1, vehicleId);
                updateVehiclePstmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    // Ignore rollback exception
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public double calculateBill(int vehicleId, int days) throws SQLException {
        VehicleDAO vehicleDAO = new VehicleDAO();
        Vehicle vehicle = vehicleDAO.searchVehicle(vehicleId);
        if (vehicle != null) {
            return vehicle.calculateRentalAmount(days);
        } else {
            throw new SQLException("Vehicle not found with ID: " + vehicleId);
        }
    }

    public List<Rental> getAllRentals() throws SQLException {
        List<Rental> list = new ArrayList<>();
        String sql = "SELECT * FROM rental";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Rental(
                        rs.getInt("rental_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("vehicle_id"),
                        rs.getDate("rent_date"),
                        rs.getDate("return_date"),
                        rs.getInt("days"),
                        rs.getDouble("total_amount"),
                        rs.getString("status")
                ));
            }
        }
        return list;
    }

    public boolean updateVehicleAvailability(int vehicleId, String availability) throws SQLException {
        VehicleDAO vehicleDAO = new VehicleDAO();
        return vehicleDAO.updateAvailability(vehicleId, availability);
    }

    public int getActiveRentalId(int customerId, int vehicleId) throws SQLException {
        String sql = "SELECT rental_id FROM rental WHERE customer_id = ? AND vehicle_id = ? AND status = 'Rented' LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, vehicleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("rental_id");
                }
            }
        }
        return -1;
    }
}
