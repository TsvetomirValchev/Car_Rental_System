package cars;

import java.time.LocalDateTime;
import java.util.Optional;

public class Trip {

    private final Integer id;
    private final int clientId;
    private final int carId;
    private final LocalDateTime rentTime;
    private final Optional<LocalDateTime> returnTime;

    public Trip(Integer id, int clientId, int carId, LocalDateTime rentTime, Optional<LocalDateTime> returnTime) {
        this.id = id;
        this.clientId = clientId;
        this.carId = carId;
        this.rentTime = rentTime;
        this.returnTime = returnTime;
    }

    public Integer getId() {
        return id;
    }
    public int getClientId() {
        return clientId;
    }
    public int getCarId() {
        return carId;
    }
    public LocalDateTime getRentTime() {
        return rentTime;
    }
    public Optional<LocalDateTime> getReturnTime() {
        return returnTime;
    }

    @Override
    public String toString() {
        return "|" + " " +
                getCarId()+ " | "+
                getRentTime() + " | "+
                getReturnTime().orElse(null);
    }
}