package db;

import cars.Trip;
import cars.Car;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class RentalController{

    private static final CarDAO carDAO = new CarDAO();
    private static final TripDAO tripDAO = new TripDAO();
    private final Car carModel;

    public RentalController(Car carModel){
        this.carModel = carModel;
    }

    public boolean isCarFreeToRent(){
        return carModel.getClientEmail() == null;
    }

    public static Trip getTripByClient(String email){
        for(Trip t: tripDAO.read().values()){
            if(t.getClientEmail().equals(email)){
                return t;
            }
        }
        return null;
    }

    public static Car getTripCar(String email) {
        for (Trip t : tripDAO.read().values()) {
            if (t.getClientEmail().equals(email) && t.getReturnTime().isEmpty()){
                return getCarByModel(t.getModel());
            }
        }
        return null;
    }

    public static Car getCarByID(String id){
        for(Car car: readCars().values()){
            if(car.getId().equals(id)){
                return car;
            }
        }
        return null;
    }

    public static Car getCarByModel(String model){
        for(Car c : readCars().values()){
            if(c.getModel().equals(model)){
                return c;
            }
        }
        return null;
    }

    public static Map<String,Car> readCars(){
        return carDAO.read();
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
