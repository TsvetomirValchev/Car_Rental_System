package db;

import db.exceptions.ExceptionTransmitter;
import rental.Car;
import rental.Trip;
import db.exceptions.AlreadyRentingException;
import db.exceptions.NotRentingException;
import logging.LoggerManager;
import users.Admin;
import users.Client;
import view.ClientDashboard;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientController implements ExceptionTransmitter {

    private static final Logger LOGGER = LoggerManager.getLogger(ClientController.class.getName());
    private static final EntryDAO<Car> carDAO = new CarDAO();
    private static final EntryDAO<Trip> tripDAO = new TripDAO();
    private static final EntryDAO<Client> clientDAO = new ClientDAO();
    private final ClientDashboard clientDashboard = new ClientDashboard(this);

    private final String clientEmail;

    public ClientController(String clientEmail) {
        this.clientEmail = clientEmail;
    }


    public Client getClient() {
        try {
            for(Client c: clientDAO.read().values()){
                if(c.getEmail().equals(clientEmail)){
                    return c;
                }
            }
        } catch (SQLException e) {
            transmitException(e,Level.SEVERE,"Couldn't find account!");
        }
        return null;
    }

    public void rentCar(int carId) {
        try{
            if (getCarByID(carId)== null) {
                System.err.println("Car with ID " + carId + " not found!");
                return;
            }
            if (!isUserCurrentlyRenting() && getCarByID(carId).getClientId()==0) {
                AdminController adminController = new AdminController(Admin.getInstance());
                adminController.addTrip(
                        new Trip(null,
                                getClient().getId(),
                                getCarByID(carId).getId(),
                                LocalDateTime.now(),
                                Optional.empty()));
                updateCarRentalStatus(getCarByID(carId), false);
            }else{
                throw new AlreadyRentingException();
            }
        }catch (AlreadyRentingException e){
            transmitException(e,Level.SEVERE,"You are already renting!");
        }
    }
    public void returnCar() {
        try{
            if (isUserCurrentlyRenting()) {
                if (getRentedCar() != null) {
                    updateCarRentalStatus(getRentedCar(), true);
                }
                for (Trip t : getHistory()) {
                    if (getClient().getId() == t.getClientId() && t.getReturnTime().isEmpty()) {
                        tripDAO.update(t.getId(), 4, LocalDateTime.now());
                        break;
                    }
                }
            } else {
                throw new NotRentingException();
            }
        }catch (NotRentingException | SQLException e){
            if(e instanceof SQLException){
                transmitException(e,Level.SEVERE,"Couldn't update trip!");
            }else {
                transmitException(e,Level.WARNING,"You are not renting a car!");
            }
        }
    }
    void updateCarRentalStatus(Car car, boolean isFree){
        try{
            if(isFree){
                carDAO.update(car.getId(),5,null);
            }
            if(!isFree){
                carDAO.update(car.getId(),5,getClient().getId());
            }
        } catch (SQLException e) {
            transmitException(e,Level.SEVERE,"Couldn't update car status!");
        }
    }

    public double calculateTripPrice(Trip trip) {
       try{
           Duration duration = Duration.between(trip.getRentTime(), trip.getReturnTime().orElse(LocalDateTime.now()));
           long hours = (long) Math.ceil((double) (duration.toMinutes() + 60) / 60.0);
           return getRentedCar().getPricePerHour() * hours;
       }catch (NotRentingException e){
           transmitException(e,Level.WARNING,"You are not renting a car!");
       }
       return 0;
    }

    public List<Trip> getHistory(){
        List<Trip> tripHistory = new ArrayList<>();
        try {
            for (Trip trip : tripDAO.read().values()) {
                if (trip.getClientId()==(getClient().getId())) {
                    tripHistory.add(trip);
                }
            }
        } catch (SQLException e) {
            transmitException(e,Level.SEVERE,"Couldn't load trip history!");
        }
        tripHistory.sort((t1, t2) -> t2.getRentTime().compareTo(t1.getRentTime()));
        return tripHistory;
    }

    public Trip getCurrentTrip(){
        if(isUserCurrentlyRenting()){
            try {
                for(Trip trip: tripDAO.read().values()){
                    if(trip.getClientId()== getClient().getId() && getRentedCar().getId()==trip.getCarId()){
                        return trip;
                    }
                }
            } catch (SQLException e) {
                transmitException(e,Level.SEVERE,"Couldn't load current trip data!");
            }
        }
        return null;
    }

    public Car getRentedCar() {
        try {
            if (isUserCurrentlyRenting()) {
                return carDAO.read().values().stream()
                        .filter(car -> car != null && car.getClientId() != null &&
                                car.getClientId().equals(getClient().getId()))
                        .findFirst()
                        .orElse(null);
            } else {
                throw new NotRentingException();
            }
        } catch (SQLException | NotRentingException e) {
            if (e instanceof SQLException) {
                transmitException(e, Level.SEVERE, "Couldn't load currently rented car data!");
            } else {
                transmitException(e, Level.WARNING, "You are not renting a car!");
            }
        }
        return null;
    }


    boolean isUserCurrentlyRenting() {
        try {
            for (Trip trip : tripDAO.read().values()) {
                if (trip.getClientId()==(getClient().getId()) && trip.getReturnTime().isEmpty()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            transmitException(e,Level.SEVERE,"Couldn't load trip history!");
        }
        return false;
    }

    public List<Car> getFreeCars() {
        try {
            return new ArrayList<>(carDAO.read().values())
                    .stream()
                    .filter(car -> car.getClientId() == 0)
                    .sorted(Comparator.comparing(Car::getMake))
                    .toList();
        } catch (SQLException e) {
            transmitException(e,Level.SEVERE,"Couldn't load car list!");
        }
        return Collections.emptyList();
    }

    public Car getCarByID(int carId) {
        try {
            for (Car car : carDAO.read().values()) {
                if (car.getId() == carId) {
                    return car;
                }
            }
        } catch (SQLException e) {
            transmitException(e,Level.SEVERE,"Couldn't find car!");
        }
        return null;
    }

    @Override
    public void transmitException(Exception e, Level severity, String message) {
        clientDashboard.printExceptionMessage(message);
    }
    @Override
    public void logException(Exception e,Level severity){
        LOGGER.log(severity,e.getMessage());
    }
}