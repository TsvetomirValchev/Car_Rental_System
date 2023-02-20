package view;

import cars.Car;
import cars.Trip;
import db.ClientController;
import db.RentalController;
import logging.LoggerManager;

import java.util.Scanner;
import java.util.logging.Logger;

public class ClientDashboard implements Dashboard {

    private static final Logger LOGGER = LoggerManager.getLogger(ClientController.class.getName());
    private final ClientController clientController;

    ClientDashboard(ClientController clientController) {
        this.clientController = clientController;
    }

    @Override
    public void printMenu(){
        System.out.println("\n'Budget Spark v1.0'\n");
        System.out.println("(1)-> View cars up for rent");
        System.out.println("(2)-> View renting history");
        System.out.println("(3)-> Rent a car");
        System.out.println("(4)-> Return rented car");
        System.out.println("(0)-> Exit");
    }

    @Override
    public void getOptions(){
        Scanner scan = new Scanner(System.in);
        int choice;
        do{
            choice =scan.nextInt();
            switch (choice){
                case 1-> printFreeCars();
                case 2-> printHistory();
                case 3-> rentalPrompt();
                case 4-> returnPrompt();
                case 0-> System.out.println("Exiting..");
                default -> System.err.println("Enter a valid option!");
            }
        }while (choice!=0);
    }

    private void printFreeCars(){
        RentalController.getFreeCars()
                .forEach(System.out::println);
    }

    private void printHistory(){
        clientController.getHistory()
                .forEach(System.out::println);
    }

    private void rentalPrompt(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the ID of the car you wish to rent:  ");
        String id = scan.nextLine();
        clientController.rentCar(RentalController.getCarByID(id));
        System.out.println("Car successfully rented!");
    }

    private void returnPrompt(){
        try{
            String clientEmail = clientController.getClient().getEMail();
            Car tripCar = RentalController.getTripCar(clientEmail);
            Trip trip = RentalController.getTripByClient(clientEmail);
            RentalController rentalController = new RentalController(tripCar);

            clientController.returnCar(clientController.getRentedCar());
            System.out.println(tripCar.getMake()+" "+tripCar.getModel()+ " returned, total price: ");
            System.out.println(rentalController.calculateTripPrice(trip)+" BGN");
        }catch (NullPointerException e){
            LOGGER.warning(e.getMessage());
        }
    }
}
