package db;

import rental.Car;
import rental.Trip;

import java.time.Duration;
import java.time.LocalDateTime;

public class RentalController {
    private final Car carModel;

    public RentalController(Car carModel) {
        this.carModel = carModel;
    }

    public double calculateTripPrice(Trip trip) {
        Duration duration = Duration.between(trip.getRentTime(), trip.getReturnTime().orElse(LocalDateTime.now()));
        long hours = (long) Math.ceil((double) (duration.toMinutes() + 60) / 60.0);
        return carModel.getPricePerHour() * hours;
    }
}
