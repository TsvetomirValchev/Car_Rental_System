package view;

import cars.Car;
import db.*;
import logging.LoggerManager;
import users.Client;
import view.abstractions.Dashboard;

import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;

public class AdminDashboard implements Dashboard {

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
            choice = scan.nextInt();
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

    private void deleteACarPrompt(){
        try{
            readAllCars();
            printSeparator(80);
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
            readAllUsers();
            printSeparator(80);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the e-mail of the user: ");
            String email = scanner.nextLine();
            adminController.deleteClient(email);
        } catch (InputMismatchException e) {
            LOGGER.warning(e.getMessage());
            System.err.println(e.getMessage());
            deleteAUserPrompt();
        }
        System.out.println("User and their history deleted!");
    }

    private void readAllCars(){
        System.out.println("All currently available cars:\n");
        adminController.getAllCars()
                .values()
                .stream()
                .sorted(Comparator.comparing(Car::getMake))
                .forEach(System.out::println);
    }

    private void readAllUsers(){
        System.out.println("All currently registered clients:\n");
        adminController.getAllClients()
                .values()
                .stream()
                .sorted(Comparator.comparing(Client::getUsername))
                .forEach(System.out::println);
    }

    private void addACarPrompt(){
        try{
            Scanner scanner = new Scanner(System.in);
            System.out.println("Adding a new car..");
            printSeparator(80);

            System.out.println("All available brands:\n");
            adminController.getAllBrands().forEach(System.out::println);
            printSeparator(80);

            System.out.println("Enter the brand of the car:");
            String brand = scanner.nextLine();

            printSeparator(80);
            System.out.println("All available models from this brand:");
            adminController.getModelsFromBrand(brand).forEach(System.out::println);
            printSeparator(80);

            System.out.println("Enter the model of the car:");
            String model = scanner.nextLine();

            System.out.println("Enter the price/hour of the car:");
            String pricePerHrStr = scanner.next();
            double pricePerHr = Double.parseDouble(pricePerHrStr);

            adminController.addCar(brand,model,pricePerHr);
            System.out.println("Car successfully added!");
            getOptions();
        }catch (InputMismatchException | NumberFormatException e){
            LOGGER.warning(e.getMessage());
            if(e instanceof  NumberFormatException){
                System.err.println("Invalid price!");
            }else{
                System.err.println("Invalid input!");
            }
            addACarPrompt();
        }
    }

    @Override
    public void printExceptionMessage(String message){
        System.err.println(message);
        printSeparator(80);
        if(message.contains("brand")){
            addACarPrompt();
        }else{
            getOptions();
        }
    }
}