import java.sql.Date;

public class Rental implements Rentable {
    private int rentalId;
    private int customerId;
    private int vehicleId;
    private Date rentDate;
    private Date returnDate;
    private int days;
    private double totalAmount;
    private String status; // "Rented", "Returned"

    public Rental() {
    }

    public Rental(int rentalId, int customerId, int vehicleId, Date rentDate, Date returnDate, int days, double totalAmount, String status) {
        this.rentalId = rentalId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.rentDate = rentDate;
        this.returnDate = returnDate;
        this.days = days;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters and Setters
    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Date getRentDate() {
        return rentDate;
    }

    public void setRentDate(Date rentDate) {
        this.rentDate = rentDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Implement Rentable interface methods
    @Override
    public void rentVehicle() {
        this.status = "Rented";
        System.out.println("Rental initiated: Vehicle ID " + vehicleId + " rented to Customer ID " + customerId);
    }

    @Override
    public void returnVehicle() {
        this.status = "Returned";
        System.out.println("Rental finalized: Vehicle ID " + vehicleId + " returned by Customer ID " + customerId);
    }

    @Override
    public String toString() {
        return "Rental{" +
                "rentalId=" + rentalId +
                ", customerId=" + customerId +
                ", vehicleId=" + vehicleId +
                ", rentDate=" + rentDate +
                ", returnDate=" + returnDate +
                ", days=" + days +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                '}';
    }
}
