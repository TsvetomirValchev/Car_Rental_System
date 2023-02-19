package db.util;

import cars.Car;
import cars.Trip;
import db.AdminController;
import db.CarDAO;
import db.EntryDAO;
import db.TripDAO;
import logging.LoggerManager;
import users.Client;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class ClientController {

    private static final Logger LOGGER = LoggerManager.getLogger(ClientController.class.getName());
    private static final EntryDAO<Car> carDAO = new CarDAO();
    private static final EntryDAO<Trip> tripDAO = new TripDAO();

    private final Client clientModel;

    public ClientController(Client clientModel) {
        this.clientModel = clientModel;
    }

    public Client getClient() {
        return clientModel;
    }

    public void rentCar(Car car) {
       if(!isUserCurrentlyRenting()){
           AdminController adminController = new AdminController();
           adminController.addTrip(
                   new Trip("",
                           clientModel.getEMail(),
                           car.getMake(),
                           car.getModel(),
                           LocalDateTime.now(),
                           Optional.empty()));
           updateCarRentalStatus(car, true);
       }
    }
    public void returnCar(Car car) {
        if(isUserCurrentlyRenting()){
            for(Trip t: getHistory()){
                if(t.getReturnTime().isEmpty()){
                    tripDAO.update(t.getId(),5,LocalDateTime.now());
                }
            }
            updateCarRentalStatus(car, false);
        }
    }
    private void updateCarRentalStatus(Car car, boolean isRent) {
        RentalController carController = new RentalController(car);
        try {
            if (isRent == carController.isCarFreeToRent()) {
                carDAO.update(car.getId(), 4, isRent ? clientModel.getEMail() : null);
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
            if (isRent) {
                LOGGER.finest("Car is already taken!");
            } else {
                LOGGER.finest("Car was never rented!");
            }
        }
    }

    public List<Trip> getHistory(){
        List<Trip> tripHistory = new ArrayList<>();
        for (Trip trip : tripDAO.read().values()) {
            if (trip.getClientEmail().equals(clientModel.getEMail())) {
                tripHistory.add(trip);
            }
        }
        tripHistory.sort((t1, t2) -> t2.getRentTime().compareTo(t1.getRentTime()));
        return tripHistory;
    }

    public Car getRentedCar(){
       if(isUserCurrentlyRenting()){
           for(Car car: carDAO.read().values()){
               if (car != null && car.getClientEmail() != null && car.getClientEmail().equals(clientModel.getEMail())){
                   return car;
               }
           }
       }
        return null;
    }

    private boolean isUserCurrentlyRenting() {
        for (Trip trip : tripDAO.read().values()) {
            if (trip.getClientEmail().equals(clientModel.getEMail()) && trip.getReturnTime().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void requestAccountDeletion(){
        new AdminController().deleteClient(clientModel.getEMail());
    }
}
