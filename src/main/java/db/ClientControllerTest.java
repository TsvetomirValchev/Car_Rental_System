package db;

import org.junit.platform.commons.function.Try;
import rental.Car;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientControllerTest {

    private static final ClientController clientController = new ClientController("acho@abv.bg");
    private static final Car car = clientController.getCarByID(7);

    @Test
    void testGetClient(){
        assertNotNull(clientController.getClient());
        System.out.println(clientController.getClient());
    }

    @Test
    void testRent(){
//        assertFalse(clientController.isUserCurrentlyRenting());
//        clientController.rentCar(car);
//        assertTrue(clientController.isUserCurrentlyRenting());
//        assertNotNull(clientController.getRentedCar());
//        System.out.println(clientController.getRentedCar());


        clientController.returnCar();
        assertNull(clientController.getRentedCar());


    }

    @Test
    void testHistory() {
        System.out.println(car.getClientId());
    }

    @Test
    void testIsUserCurrentlyRenting(){
        catcher();
    }

    @Test
    void testUpdateRentalStatus() {
        clientController.updateCarRentalStatus(car,false);
    }

    @Test
    void getFreeCars(){
        System.out.println(car.getClientId());
    }

    void throwException1(){
        throw new RuntimeException("Kukuruz");
    }
    void rethrow(){
        throwException1();
    }
    void catcher(){
        try{
           rethrow();
        }catch (RuntimeException e ){
            System.out.println(e.getMessage());
        }
    }

}