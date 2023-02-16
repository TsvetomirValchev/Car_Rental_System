package cars;

import java.time.LocalDateTime;
import java.util.Optional;

public class Trip extends Rental {

    private final LocalDateTime rentTime;
    private final Optional<LocalDateTime> returnTime;
    private final String rentID;

    public Trip(String rentID,String clientEmail,String make, String model, LocalDateTime rentTime, Optional<LocalDateTime> returnTime){
        super(make, model, clientEmail);
        this.rentID=rentID;
        this.rentTime=rentTime;
        this.returnTime= returnTime;
    }

    public LocalDateTime getRentTime() {
        return rentTime;
    }
    public Optional<LocalDateTime> getReturnTime() {
        return returnTime;
    }
    public String getId() {
        return rentID;
    }

    @Override
    public String toString() {
        return "|" + " " +
                this.getMake() + " | " +
                this.getModel() + " | "+
                this.getRentTime() + " | "+
                this.getReturnTime().orElse(null);
    }
}