package db;

import rental.Trip;
import rental.Car;
import users.Client;
import org.junit.jupiter.api.Test;


import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

class EntryDAOTest {

    private static final EntryDAO<Client> clientDAO = new ClientDAO();
    private static final EntryDAO<Car> carDAO = new CarDAO();

    private static final EntryDAO<Trip> tripDAO = new TripDAO();

    @Test
    void testReading() throws SQLException {
        clientDAO.read().forEach((k,v)->System.out.println(v));
        System.out.println();
        carDAO.read().forEach((k,v)->System.out.println(v));
        System.out.println();
        tripDAO.read().forEach((k,v)->System.out.println(v));

    }

    @Test
    void testUpdate() throws SQLException {
        tripDAO.update(29,4,LocalDateTime.now());
    }

    @Test
    void testDelete() throws SQLException {
        clientDAO.delete("slavchev@abv.bg");
    }

    @Test
    void testCreate() throws SQLException {
//        carDAO.create(new Car(3,"Ford","Focus",11.3,null,true));
        clientDAO.create(new Client(null,"ivan","parola2001",LocalDate.parse("2000-01-01"),"vanko@abv.bg"));
    }
//
//    @Test
//    void isCarFreeTest() {
//        Car car = new Car("CB4000AS","Bruh","Bruh",null,230);
//        RentalController carController = new RentalController(car);
//        Assertions.assertTrue(carController.isCarFreeToRent());
//        Assertions.assertFalse(carController.isCarFreeToRent());
//    }
//
//    @Test
//    void testRent() {
//        Client client = new Client
//                ("Vanio","passered123", LocalDate.parse("2001-01-01"),"nasence@abv.bg");
//        ClientController clientController = new ClientController(client);
//        clientController.returnCar(new Car("LP2323PL","Mercedes","S65","nasence@abv.bg",14));
//    }
//
//    @Test
//    void testPrice(){
//        Car car = new Car("test","test","test","petko@abv.bg",23);
//        Trip trip = new Trip("23","petko@abv.bg","test","test",
//                LocalDateTime.parse("2021-11-03T04:34"),Optional.of(LocalDateTime.parse("2021-11-03T06:36")));
//        RentalController rentalController = new RentalController(car);
//        System.out.println(rentalController.calculateTripPrice(trip));
//    }
//
//    @Test
//    void testWhatever() {
//        ClientController clientController =
//                new ClientController(new Client("Patso","patso123",LocalDate.parse("2000-01-01"),"abv@abv.abv"));
//        System.out.println(clientController.getRentedCar());
//    }
//
//    @Test
//    void testRegex() {
//        Assertions.assertTrue(UserValidator.isNameValid("user123"));
//    }
}