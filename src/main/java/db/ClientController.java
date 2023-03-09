package db;

import db.exceptions.ExceptionTransmitter;
import rental.Car;
import rental.Trip;
import db.exceptions.AlreadyRentingException;
import db.exceptions.NotRentingException;
import logging.LoggerManager;
import users.Client;
import view.ClientDashboard;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
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
            LOGGER.severe(e.getMessage());
            transmitException(e);
        }
        return null;
    }

    public void rentCar(Car car) {
      try{
          if (!isUserCurrentlyRenting() && car.getClientId()==0) {
              AdminController adminController = new AdminController();
              adminController.addTrip(
                      new Trip(null,
                              getClient().getId(),
                              car.getId(),
                              LocalDateTime.now(),
                              Optional.empty()));
              updateCarRentalStatus(car, false);
          }else{
              throw new AlreadyRentingException();
          }
      }catch (AlreadyRentingException e){
          LOGGER.severe(e.getMessage());
          transmitException(e);
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
            LOGGER.severe(e.getMessage());
            transmitException(e);
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
            LOGGER.severe(e.getMessage());
            transmitException(e);
        }
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
            LOGGER.severe(e.getMessage());
            transmitException(e);
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
                LOGGER.severe(e.getMessage());
                transmitException(e);
            }
        }
        return null;
    }

    public Car getRentedCar(){
        if(isUserCurrentlyRenting()){
            try {
                for(Car car: carDAO.read().values()){
                    if (car != null && car.getClientId() != null && car.getClientId().equals(getClient().getId())){
                        return car;
                    }
                }
            } catch (SQLException e) {
                LOGGER.severe(e.getMessage());
                transmitException(e);
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
            LOGGER.severe(e.getMessage());
            transmitException(e);
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
            LOGGER.severe(e.getMessage());
            transmitException(e);
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
            LOGGER.severe(e.getMessage());
            transmitException(e);
        }
        return null;
    }

    @Override
    public void transmitException(Exception e){
        clientDashboard.printExceptionMessage(e);
    }
}