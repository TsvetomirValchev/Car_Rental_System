package cars;

public class RentalCar extends Car {

    private final Integer id;
    private final Double pricePerHour;
    private final Integer clientId;
    private final boolean isFree;

    public RentalCar(Integer id, String make, String model, Double pricePerHour, Integer clientId, boolean isFree) {
        super(make,model);
        this.id = id;
        this.pricePerHour = pricePerHour;
        this.clientId = clientId;
        this.isFree = isFree;
    }

    public Integer getId() {
        return id;
    }
    public double getPricePerHour() {
        return pricePerHour;
    }
    public Integer getClientId() {
        return clientId;
    }

    @Override
    public String toString() {
        String status = isFree ? "Free" : "Booked";
        return "|" + " " +
                getId() + " | " +
                getMake() + " | " +
                getModel() + " | "+
                getPricePerHour()+" BGN/Hr | "+
                "Status: " + status;
    }
}