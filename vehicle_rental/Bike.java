public class Bike extends Vehicle {

    public Bike() {
        super();
        setVehicleType("Bike");
    }

    public Bike(int vehicleId, String vehicleName, double rentPerDay, String available) {
        super(vehicleId, vehicleName, "Bike", rentPerDay, available);
    }

    @Override
    public double calculateRentalAmount(int days) {
        return days * getRentPerDay();
    }
}
