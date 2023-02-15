package cars;

abstract class Rental {

    private String make;
    private String model;
    private String clientEmail;

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
