package view;

import rental.Car;
import db.*;
import logging.LoggerManager;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;

public class AdminDashboard implements Dashboard{

    private static final Logger LOGGER = LoggerManager.getLogger(AdminDashboard.class.getName());
    private final AdminController adminController;

    public AdminDashboard(AdminController adminController) {
        this.adminController = adminController;
    }

    @Override
    public void printMenu() {
        System.out.println("\n'Budget Spark v1.0'\n");
        System.out.println("(1)-> View all cars");
        System.out.println("(2)-> View all users");
        System.out.println("(3)-> Add a car");
        System.out.println("(4)-> Delete a car");
        System.out.println("(5)-> Delete a user");
        System.out.println("(0)-> Exit");
    }

    @Override
    public void getOptions() {
        Scanner scan = new Scanner(System.in);
        int choice;
        do{
            printMenu();
            choice =scan.nextInt();
            printSeparator(80);
            switch (choice){
                case 1-> readAllCars();
                case 2-> readAllUsers();
                case 3-> addACarPrompt();
                case 4-> deleteACarPrompt();
                case 5-> deleteAUserPrompt();
                case 0-> System.out.println("Exiting..");
                default -> System.err.println("Enter a valid option!");
            }
            if(choice!=0){
                printSeparator(80);
            }
        }while (choice!=0);
    }

    private void readAllCars(){
        System.out.println("All currently available cars: ");
        adminController.getAllCars().forEach((k,v)->System.out.println(v));
    }

    private void readAllUsers(){
        System.out.println("All currently registered clients: ");
        adminController.readAllClients().forEach((k,v)->System.out.println(v));
    }

    private void addACarPrompt(){
        try{
            Scanner scanner = new Scanner(System.in);
            System.out.println("Adding a new car");
            System.out.println();

            System.out.println("Enter brand and model: ");
            String[] carInfo = scanner.nextLine().split(" ");
            String brand = carInfo[0];
            String model = carInfo[1];

            System.out.println("Enter car price per hour: ");
            String pricePerHrStr = scanner.next();
            double pricePerHr = Double.parseDouble(pricePerHrStr);

            adminController.addCar(new Car(null,brand,model,pricePerHr,null,true));
            System.out.println("Car successfully added!");

        }catch (InputMismatchException | ArrayIndexOutOfBoundsException e){
            LOGGER.warning(e.getMessage());
            System.err.println(e.getMessage());
            addACarPrompt();
        }
    }

    private void deleteACarPrompt(){
        try{
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the ID of the car: ");
            int id = scanner.nextInt();
            adminController.deleteCar(id);
        }catch (InputMismatchException e){
            LOGGER.warning(e.getMessage());
            System.err.println(e.getMessage());
            deleteACarPrompt();
        }
        System.out.println("Car deleted!");
    }

    private void deleteAUserPrompt() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the e-mail of the user: ");
            String email = scanner.nextLine();
            adminController.deleteClient(email);
        } catch (InputMismatchException e) {
            LOGGER.warning(e.getMessage());
            System.err.println(e.getMessage());
            deleteAUserPrompt();
        }
        System.out.println("User deleted!");
    }

    @Override
    public void printExceptionMessage(String message){
        System.err.println(message);
        printSeparator(80);
        getOptions();
    }
}