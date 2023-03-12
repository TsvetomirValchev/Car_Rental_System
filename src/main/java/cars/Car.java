package cars;

public class Car {
    private final String make;
    private final String model;

    public Car(String make, String model) {
        this.make = make;
        this.model = model;
    }

    public String getMake() {
        return make;
    }
    public String getModel() {
        return model;
    }

    @Override
    public String toString() {
        return "|" + " " +
                getMake() + " | " +
                getModel() + " | ";
    }
}
