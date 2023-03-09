package db;

import db.exceptions.ExceptionTransmitter;
import logging.LoggerManager;
import rental.Car;
import rental.Trip;
import users.Admin;
import users.Client;
import view.AdminDashboard;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

public class AdminController implements ExceptionTransmitter {

    private static final Logger LOGGER = LoggerManager.getLogger(AdminController.class.getName());
    private static final EntryDAO<Client> clientDAO = new ClientDAO();
    private static final EntryDAO<Car> carDAO = new CarDAO();
    private static final EntryDAO<Trip> tripDAO = new TripDAO();
    private final AdminDashboard adminDashboard = new AdminDashboard(this);
    private final Admin adminModel;

    public AdminController() {
        this.adminModel = Admin.getInstance();
    }

    public Map<Object, Client> readAllClients(){
        try {
            return clientDAO.read();
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
            transmitException(e);
        }
        return Collections.emptyMap();
    }

    public void addClient(Client client){
        try {
            clientDAO.create(client);
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
            transmitException(e);
        }
    }

    public void addCar(Car car){
//        if(CarValidator.isCarIDValid(car.getId())){
        try {
            carDAO.create(car);
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
            transmitException(e);
        }
//        }
    }

    public Map<Object, Car> getAllCars() {
        try {
            return carDAO.read();
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
            transmitException(e);
        }
        return Collections.emptyMap();
    }

    public void deleteClient(Object id){
        try {
            clientDAO.delete(id);
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
            transmitException(e);
        }
    }

    public void deleteCar(int carId){
        try {
            carDAO.delete(carId);
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
            transmitException(e);
        }
    }

    public void addTrip(Trip trip){
        try {
            tripDAO.create(trip);
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
            transmitException(e);
        }
    }

    public Client getClientByUsername(String username) {
        Map<Object, Client> clients = readAllClients();
        for (Client c : clients.values()) {
            if (c.getUsername().equals(username)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public void transmitException(Exception e) {
        adminDashboard.printExceptionMessage(e);
    }
}
