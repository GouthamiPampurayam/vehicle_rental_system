public abstract class Vehicle {
    private int vehicleId;
    private String vehicleName;
    private String vehicleType;
    private double rentPerDay;
    private String available; // "Yes" or "No"

    // Default constructor
    public Vehicle() {
    }

    // Parameterized constructor
    public Vehicle(int vehicleId, String vehicleName, String vehicleType, double rentPerDay, String available) {
        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
        this.vehicleType = vehicleType;
        this.rentPerDay = rentPerDay;
        this.available = available;
    }

    // Getters and Setters
    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getRentPerDay() {
        return rentPerDay;
    }

    public void setRentPerDay(double rentPerDay) {
        this.rentPerDay = rentPerDay;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    // Abstract method to calculate rental amount
    public abstract double calculateRentalAmount(int days);

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId=" + vehicleId +
                ", vehicleName='" + vehicleName + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", rentPerDay=" + rentPerDay +
                ", available='" + available + '\'' +
                '}';
    }
}
