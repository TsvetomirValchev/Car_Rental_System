package db.util;

import cars.Trip;
import db.CarDAO;
import cars.Car;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RentalController{

    private static final CarDAO carDAO = new CarDAO();
    private final Car carModel;

    public RentalController(Car carModel){
        this.carModel = carModel;
    }

    public boolean isCarFreeToRent(){
        return carModel.getClientEmail() == null;
    }

    public static List<Car> getFreeCars(){
        return new ArrayList<>(carDAO.read().values())
                .stream()
                .filter(car -> car.getClientEmail() == null)
                .sorted(Comparator.comparing(Car::getMake))
                .toList();
    }

    public double calculateTripPrice(Trip trip) {
        Duration duration = Duration.between(trip.getRentTime(), trip.getReturnTime().orElse(LocalDateTime.now()));
        long hours = (long) Math.ceil((double) (duration.toMinutes() + 60) / 60.0);
        return carModel.getPricePerHour() * hours;
    }
}
