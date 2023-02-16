package db;

import cars.Car;
import cars.Trip;
import cars.util.CarValidator;
import users.Admin;
import users.Client;

public class AdminController {

    private static final EntryDAO<Client> clientDAO = new ClientDAO();
    private static final EntryDAO<Car> carDAO = new CarDAO();
    private static final EntryDAO<Trip> tripDAO = new TripDAO();
    private final Admin adminModel;

    public AdminController() {
        this.adminModel = Admin.getInstance();
    }

    public void addClient(Client client){
        clientDAO.create(client);
    }

    public void addCar(Car car){
        if(CarValidator.isCarBrandValid(car.getMake()) && CarValidator.isCarIDValid(car.getId())){
            carDAO.create(car);
        }
    }

    public void deleteClient(String email){
        clientDAO.delete(email);
    }

    public void deleteCar(String licensePlate){
        carDAO.delete(licensePlate);
    }

    public void addTrip(Trip trip){tripDAO.create(trip);}

}
