package db;

import cars.Car;
import cars.Trip;
import users.Admin;
import users.Client;

public class AdminController {

    private static final EntryDAO<Client> clientDAO = new ClientDAO();
    private static final EntryDAO<Car> carDAO = new CarDAO();
    private static final EntryDAO<Trip> tripDAO = new TripDAO();
    private Admin adminModel;

    public AdminController() {
        this.adminModel = Admin.getInstance();
    }

    public void addClient(Client client){
        clientDAO.create(client);
    }

    public void addCar(Car car){
        carDAO.create(car);
    }

    public void deleteClient(String email){
        clientDAO.delete(email);
    }

    public void deleteCar(String licensePlate){
        carDAO.delete(licensePlate);
    }

    public void addTrip(Trip trip){tripDAO.create(trip);}

}
