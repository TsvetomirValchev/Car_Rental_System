package cars;

import java.time.LocalDateTime;

public class Trip extends Rental {

    private LocalDateTime rentTime;
    private LocalDateTime returnTime;
    private String rentID;

    public Trip(String rentID,String clientEmail,String make, String model, LocalDateTime rentTime, LocalDateTime returnTime){
        super(make, model, clientEmail);
        this.rentID=rentID;
        this.rentTime=rentTime;
        this.returnTime=returnTime;
    }

    public LocalDateTime getRentTime() {
        return rentTime;
    }
    public LocalDateTime getReturnTime() {
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
                this.getReturnTime();
    }
}