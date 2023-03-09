package rental;

public class Car {

    private final Integer id;
    private final String make;
    private final String model;
    private final double pricePerHour;
    private final Integer clientId;
    private final boolean isFree;

    public Car(Integer id, String make, String model, double pricePerHour, Integer clientId, boolean isFree) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.pricePerHour = pricePerHour;
        this.clientId = clientId;
        this.isFree = isFree;
    }

    public Integer getId() {
        return id;
    }
    public String getMake() {
        return make;
    }
    public String getModel() {
        return model;
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