package db;

import cars.Car;
import cars.RentalCar;
import cars.Trip;
import db.interfaces.ExceptionTransmitter;
import logging.LoggerManager;
import users.Admin;
import users.Client;
import view.AdminDashboard;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminController implements ExceptionTransmitter {

    private static final Logger LOGGER = LoggerManager.getLogger(AdminController.class.getName());
    private static final EntryDAO<Client> clientDAO = new ClientDAO();
    private static final EntryDAO<Trip> tripDAO = new TripDAO();
    private static final CarDAO carDAO = new CarDAO();
    private final AdminDashboard adminDashboard = new AdminDashboard(this);
    private final Admin adminModel;

    public AdminController(Admin admin) {
        this.adminModel = new Admin();
    }

    public Map<Object, Client> getAllClients(){
        try {
            return clientDAO.read();
        } catch (SQLException e) {
            transmitException(e, Level.SEVERE, "Couldn't access data for clients!");
        }
        return Collections.emptyMap();
    }

    public Map<Object, RentalCar> getAllCars() {
        try {
            return carDAO.read();
        } catch (SQLException e) {
            transmitException(e, Level.SEVERE, "Couldn't access data for cars!");
        }
        return Collections.emptyMap();
    }

    public void addClient(Client client){
        try {
            clientDAO.create(client);
        } catch (SQLException e) {
            transmitException(e, Level.SEVERE, "Couldn't register client!");
        }
    }

    public void addCar(String make, String model, double pricePerHour){
        try {
            List<Car> models = getModelsFromBrand(make);
            if (models.stream().noneMatch(car -> car.getModel().equals(model))) {
                throw new IllegalArgumentException();
            }
            carDAO.create(new RentalCar(null, make, model, pricePerHour, null, true));
        } catch (SQLException | IllegalArgumentException e) {
            if (e instanceof IllegalArgumentException) {
                transmitException(e, Level.SEVERE, "No such brand-model combination!");
            } else {
                transmitException(e, Level.SEVERE, "Couldn't add car!");
            }
        }
    }

    public List<Car> getModelsFromBrand(String brand){
        List<String> brands = getAllBrands();
        try {
            if (!brands.contains(brand)) {
                throw new IllegalArgumentException();
            }
            return carDAO.readCarModels(brand)
                    .stream()
                    .sorted(Comparator.comparing(Car::getModel))
                    .toList();
        } catch (SQLException | IllegalArgumentException e) {
            if(e instanceof SQLException){
                transmitException(e, Level.SEVERE, "Couldn't load car models!");
            }else{
                transmitException(e, Level.WARNING, "Invalid brand entered: " + brand);
            }
        }
        return Collections.emptyList();
    }

    public List<String> getAllBrands(){
        try{
            return carDAO.readCarBrands()
                    .stream()
                    .map(Car::getMake)
                    .sorted()
                    .toList();
        } catch (SQLException e) {
            transmitException(e, Level.SEVERE, "Couldn't load car brands!");
        }
        return Collections.emptyList();
    }

    public void deleteClient(Object key){
        try {
            List<Trip> trips = new ArrayList<>();
            for (Trip trip : tripDAO.read().values()) {
                if (trip.getClientId()==getClientByEmail((String) key).getId()) {
                    trips.add(trip);
                }
            }
            if (!trips.isEmpty()) {
                for (Trip trip : trips) {
                    tripDAO.delete(trip.getId());
                }
            }
            clientDAO.delete(key);
        } catch (SQLException e) {
            if (e instanceof SQLDataException){
                transmitException(e,Level.WARNING,e.getMessage());
            }else{
                transmitException(e,Level.SEVERE, "Couldn't delete client account!");
            }
        }
    }

    public void deleteCar(int carId){
        try {
            carDAO.delete(carId);
        } catch (SQLException e) {
            if (e instanceof SQLDataException){
                transmitException(e,Level.WARNING,e.getMessage());
            }else{
                transmitException(e,Level.SEVERE,"Couldn't delete car!");
            }

        }
    }

    public Client getClientByUsername(String username) {
        Map<Object, Client> clients = getAllClients();
        return clients.values().stream()
                .filter(c -> c.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public Client getClientByEmail(String email) {
        Map<Object, Client> clients = getAllClients();
        return clients.values().stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
    @Override
    public void transmitException(Exception e, Level severity,String message) {
        logException(e,severity);
        adminDashboard.printExceptionMessage(message);
    }
    @Override
    public void logException(Exception e,Level severity){
        LOGGER.log(severity,e.getMessage());
    }
}
