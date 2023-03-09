package view;

import rental.Car;
import rental.Trip;
import db.ClientController;
import db.RentalController;
import logging.LoggerManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;

public class ClientDashboard implements Dashboard {

    private static final Logger LOGGER = LoggerManager.getLogger(ClientDashboard.class.getName());
    private final ClientController clientController;

    public ClientDashboard(ClientController clientController) {
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
            printMenu();
            choice =scan.nextInt();
            printSeparator(80);
            switch (choice) {
                case 1 -> printFreeCars();
                case 2 -> printHistory();
                case 3 -> rentalPrompt();
                case 4 -> returnPrompt();
                case 0 -> System.out.println("Exiting..");
                default -> System.err.println("Enter a valid option!");
            }
            if (choice != 0) {
                printSeparator(80);
            }
        }while (choice!=0);
    }

    private void printFreeCars(){
        System.out.println("All cars available for rent: ");
        clientController.getFreeCars()
                .forEach(System.out::println);
    }

    private void printHistory(){
        System.out.println(getFormattedHistory());
    }

    public String getFormattedHistory(){
        StringBuilder sb = new StringBuilder();
        for(Trip trip: clientController.getHistory()){
            Car car = clientController.getCarByID(trip.getCarId());
            if(car != null){
                String rentTime = formatTime(trip.getRentTime());
                String returnTime = trip.getReturnTime()
                        .map(this::formatTime)
                        .orElse("Ongoing trip");
                sb.append(String.format("| %s | %s | %s | %s\n",
                        car.getMake(),
                        car.getModel(),
                        rentTime,
                        returnTime));
            }
        }
        return sb.toString();
    }
    private String formatTime(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private void rentalPrompt(){
       try{
           Scanner scan = new Scanner(System.in);

           System.out.println("Enter the ID of the car you wish to rent:  ");
           int id = scan.nextInt();
           clientController.rentCar(clientController.getCarByID(id));

           System.out.println("Car successfully rented!");
       }catch (InputMismatchException e){
           LOGGER.warning(e.getMessage());
           System.err.println("Invalid input format!");
           rentalPrompt();
       }
    }

    private void returnPrompt(){
        Car tripCar = clientController.getRentedCar();
        Trip trip = clientController.getCurrentTrip();
        RentalController rentalController = new RentalController(tripCar);

        clientController.returnCar();
        System.out.println(tripCar.getMake()+" "+tripCar.getModel()+ " returned, total price: ");
        System.out.println(rentalController.calculateTripPrice(trip));
    }

    @Override
    public void printExceptionMessage(Exception e){
        System.err.println(e.getMessage());
        printSeparator(80);
        getOptions();
    }
}
