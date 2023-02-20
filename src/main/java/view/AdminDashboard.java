package view;

import cars.Car;
import db.*;
import users.Client;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AdminDashboard implements Dashboard{

    private static final EntryDAO<Car> carDAO = new CarDAO();
    private static final EntryDAO<Client> clientDAO = new ClientDAO();

    private final AdminController adminController;

    AdminDashboard(AdminController adminController) {
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
            choice =scan.nextInt();
            switch (choice){
                case 1-> readAllCars();
                case 2-> readAllUsers();
                case 3-> addACarPrompt();
                case 4-> deleteACarPrompt();
                case 5-> deleteAUserPrompt();
                case 0-> System.out.println("Exiting..");
                default -> System.err.println("Enter a valid option!");
            }
        }while (choice!=0);
    }

    private void readAllCars(){
        System.out.println("All currently available cars: ");
        carDAO.read().forEach((k,v)->System.out.println(v));
    }

    private void readAllUsers(){
        System.out.println("All currently registered clients: ");
        clientDAO.read().forEach((k,v)->System.out.println(v));
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

            System.out.println("Enter car ID: ");
            String id = scanner.nextLine();

            System.out.println("Enter car price per hour: ");
            String pricePerHrStr = scanner.next();
            double pricePerHr = Double.parseDouble(pricePerHrStr);

            adminController.addCar(new Car(id,brand,model,null,pricePerHr));
            System.out.println("Car successfully added!");

        }catch (InputMismatchException e){
            e.printStackTrace();
            addACarPrompt();
        }
    }

    private void deleteACarPrompt(){
        try{
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the ID (License plate) of the car: ");
            String id = scanner.nextLine();
            adminController.deleteCar(id);
        }catch (InputMismatchException e){
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
            System.err.println(e.getMessage());
            deleteAUserPrompt();
        }
        System.out.println("User deleted!");
    }
}
