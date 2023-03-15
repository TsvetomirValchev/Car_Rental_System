package db;

import cars.Car;
import cars.RentalCar;
import cars.Trip;
import db.abstractions.Controller;
import users.Admin;
import users.Client;
import view.AdminDashboard;
import view.abstractions.Dashboard;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

public class AdminController extends Controller {

    private static final EntryDAO<Client> clientDAO = new ClientDAO();
    private static final EntryDAO<Trip> tripDAO = new TripDAO();
    private static final CarDAO carDAO = new CarDAO();
    private final AdminDashboard adminDashboard = new AdminDashboard(this);
    private final Admin adminModel;

    public AdminController(Admin admin) {
        this.adminModel = new Admin();
    }

    @Override
    protected Dashboard getDashboard() {
        return adminDashboard;
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
                transmitException(new IllegalArgumentException(), Level.WARNING, "No such brand-model combination!");
            }
            carDAO.create(new RentalCar(null, make, model, pricePerHour, null, true));
        } catch (SQLException e) {
            transmitException(e, Level.SEVERE, "Couldn't add car!");
        }
    }

    public List<Car> getModelsFromBrand(String brand){
        List<String> brands = getAllBrands();
        try {
            if (!brands.contains(brand)) {
                transmitException(new IllegalArgumentException(), Level.WARNING, "Invalid brand entered: " + brand);
            }
            return carDAO.readCarModels(brand)
                    .stream()
                    .sorted(Comparator.comparing(Car::getModel))
                    .toList();
        } catch (SQLException | IllegalArgumentException e) {
            transmitException(e, Level.SEVERE, "Couldn't load car models!");
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
            Client client = getClientByEmail((String) key);
            if (client != null) {
                deleteTripsByField("clientId", client.getId());
                clientDAO.delete(key);
            } else {
                transmitException(new IllegalArgumentException(),Level.WARNING,"User with e-mail: '"+key+"' was not found!");
            }
        } catch (SQLException e) {
            if (e instanceof SQLDataException){
                transmitException(e,Level.WARNING,e.getMessage());
            } else {
                transmitException(e,Level.SEVERE, "Couldn't delete client account!");
            }
        }
    }

    public void deleteCar(int carId){
        try {
            RentalCar car = getAllCars().values().stream().filter((v)->v.getId()==carId).findFirst().orElse(null);
            if(!car.isFree()){
                transmitException(new IllegalStateException(),Level.WARNING,"Car is being rented!");
            }
            deleteTripsByField("carId", carId);
            carDAO.delete(carId);
        } catch (SQLException e) {
            if (e instanceof SQLDataException){
                transmitException(e,Level.WARNING,e.getMessage());
            } else {
                transmitException(e,Level.SEVERE,"Couldn't delete car!");
            }
        }
    }

    private void deleteTripsByField(String fieldName, int fieldValue) throws SQLException {
        List<Trip> trips = new ArrayList<>();
        for (Trip trip : tripDAO.read().values()) {
            if (fieldName.equals("clientId") && trip.getClientId() == fieldValue) {
                if (trip.getReturnTime().isEmpty()) {
                    transmitException(new IllegalStateException(),Level.WARNING,"User is currently renting!");
                }
                trips.add(trip);
            } else if (fieldName.equals("carId") && trip.getCarId() == fieldValue) {
                trips.add(trip);
            }
        }
        if (!trips.isEmpty()) {
            for (Trip trip : trips) {
                tripDAO.delete(trip.getId());
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

    private Client getClientByEmail(String email) {
        Map<Object, Client> clients = getAllClients();
        return clients.values().stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}