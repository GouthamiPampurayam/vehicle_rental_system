import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    public boolean addVehicle(Vehicle vehicle) throws SQLException {
        String sql = "INSERT INTO vehicle (vehicle_name, vehicle_type, rent_per_day, available) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vehicle.getVehicleName());
            pstmt.setString(2, vehicle.getVehicleType());
            pstmt.setDouble(3, vehicle.getRentPerDay());
            pstmt.setString(4, vehicle.getAvailable());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean updateVehicle(Vehicle vehicle) throws SQLException {
        String sql = "UPDATE vehicle SET vehicle_name = ?, vehicle_type = ?, rent_per_day = ?, available = ? WHERE vehicle_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vehicle.getVehicleName());
            pstmt.setString(2, vehicle.getVehicleType());
            pstmt.setDouble(3, vehicle.getRentPerDay());
            pstmt.setString(4, vehicle.getAvailable());
            pstmt.setInt(5, vehicle.getVehicleId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteVehicle(int vehicleId) throws SQLException {
        String sql = "DELETE FROM vehicle WHERE vehicle_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicleId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public Vehicle searchVehicle(int vehicleId) throws SQLException {
        String sql = "SELECT * FROM vehicle WHERE vehicle_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVehicle(rs);
                }
            }
        }
        return null;
    }

    public List<Vehicle> searchVehiclesByName(String name) throws SQLException {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicle WHERE vehicle_name LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToVehicle(rs));
                }
            }
        }
        return list;
    }

    public List<Vehicle> getAllVehicles() throws SQLException {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicle";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToVehicle(rs));
            }
        }
        return list;
    }

    public List<Vehicle> getAvailableVehicles() throws SQLException {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicle WHERE available = 'Yes'";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToVehicle(rs));
            }
        }
        return list;
    }

    public boolean updateAvailability(int vehicleId, String availability) throws SQLException {
        String sql = "UPDATE vehicle SET available = ? WHERE vehicle_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, availability);
            pstmt.setInt(2, vehicleId);
            return pstmt.executeUpdate() > 0;
        }
    }

    private Vehicle mapResultSetToVehicle(ResultSet rs) throws SQLException {
        int id = rs.getInt("vehicle_id");
        String name = rs.getString("vehicle_name");
        String type = rs.getString("vehicle_type");
        double rent = rs.getDouble("rent_per_day");
        String available = rs.getString("available");

        if ("Car".equalsIgnoreCase(type)) {
            return new Car(id, name, rent, available);
        } else {
            return new Bike(id, name, rent, available);
        }
    }
}
