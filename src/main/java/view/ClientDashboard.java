package view;

import cars.RentalCar;
import cars.Trip;
import db.ClientController;
import logging.LoggerManager;
import view.abstractions.Dashboard;

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
            choice = scan.nextInt();
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

    private void rentalPrompt(){
        try{
            Scanner scan = new Scanner(System.in);

            printFreeCars();
            printSeparator(80);

            System.out.println("Enter the ID of the car you wish to rent:  ");
            int id = scan.nextInt();

            if (clientController.rentCar(id)) {
                System.out.println("Car successfully rented!");
            }
        }catch (InputMismatchException e){
            LOGGER.warning(e.getMessage());
            System.err.println("Invalid input format!");
            rentalPrompt();
        }
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
            RentalCar rentalCar = clientController.getCarByID(trip.getCarId());
            if(rentalCar != null){
                String rentTime = formatTime(trip.getRentTime());
                String returnTime = trip.getReturnTime()
                        .map(this::formatTime)
                        .orElse("Ongoing trip");
                sb.append(String.format("| %s | %s | %s | %s\n",
                        rentalCar.getMake(),
                        rentalCar.getModel(),
                        rentTime,
                        returnTime));
            }
        }
        if(sb.length() == 0) {
            return "No trips found for this account.";
        }
        return sb.toString();
    }
    private String formatTime(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private void returnPrompt(){
        RentalCar tripRentalCar = clientController.getRentedCar();
        Trip trip = clientController.getCurrentTrip();

        System.out.println(tripRentalCar.getMake()+" "+ tripRentalCar.getModel()+ " returned, total price: ");
        System.out.println(clientController.calculateTripPrice(trip)+" BGN");

        clientController.returnCar();
    }

    @Override
    public void printExceptionMessage(String message){
        System.err.println(message);
        printSeparator(80);
        if(message.contains("ID")){
            rentalPrompt();
        }else{
            getOptions();
        }
    }
}
