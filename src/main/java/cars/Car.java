package cars;

public class Car extends Rental {

    private final String id;
    private final double pricePerHour;

    public Car(String id, String make, String model, String clientEmail,double pricePerHour) {
        super(make,model,clientEmail);
          this.id=id;
          this.pricePerHour = pricePerHour;
    }

    public String getId() {
        return id;
    }
    public double getPricePerHour() {
        return pricePerHour;
    }

    @Override
    public String toString() {
        return "|" + " " +
                getId() + " | " +
                getMake() + " | " +
                getModel() + " | "+
                getClientEmail() + " | "+
                getPricePerHour()+" BGN/Hr";
    }
}
