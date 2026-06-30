public class Car extends Vehicle {

    public Car() {
        super();
        setVehicleType("Car");
    }

    public Car(int vehicleId, String vehicleName, double rentPerDay, String available) {
        super(vehicleId, vehicleName, "Car", rentPerDay, available);
    }

    @Override
    public double calculateRentalAmount(int days) {
        return (days * getRentPerDay()) + 15.0;
    }
}
