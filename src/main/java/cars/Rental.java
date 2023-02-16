package cars;

public abstract class Rental {

    private final String make;
    private final String model;
    private final String clientEmail;

    public Rental(String make, String model, String clientEmail) {
        this.make = make;
        this.model = model;
        this.clientEmail = clientEmail;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getClientEmail() {
        return clientEmail;
    }
}
