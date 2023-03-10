package view;

import db.AdminController;
import db.ClientController;
import logging.LoggerManager;
import users.Admin;
import users.Client;
import users.util.ClientRegistrant;

import java.io.Console;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;

public class LogInMenu {
    private static final Logger LOGGER = LoggerManager.getLogger(LogInMenu.class.getName());
    private static final Admin admin = Admin.getInstance();

    public void printInitialPrompt() {
        try{
            Scanner scanner = new Scanner(System.in);

            System.out.println("Welcome!");
            System.out.println("(1) -> Login");
            System.out.println("(2) -> Register");

            int choice = scanner.nextInt();
            if (choice == 1) {
                printLoginPrompt();
            } else if (choice == 2) {
                accountCreationPrompts();
                System.out.println("Account created!");
                printLoginPrompt();
            } else {
                System.err.println("Invalid choice!");
                printInitialPrompt();
            }
        }catch (InputMismatchException e){
            LOGGER.warning(e.getMessage());
            System.err.println("Invalid input");
            printInitialPrompt();
        }
    }

    private void printLoginPrompt() {
        Scanner scanner = new Scanner(System.in);

        String username = null;
        String password = null;
        try {
            System.out.println("Enter login info:");
            String[] loginInfo = scanner.nextLine().split(" ");
            username = loginInfo[0];
            password = loginInfo[1];
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            LOGGER.warning(e.getMessage());
            System.err.println("Please enter valid data!");
            printLoginPrompt();
        }

        if (username.equals(admin.getUsername()) && password.equals(admin.getPassword())) {
            openAdminView();
        } else {
            Client client = new AdminController(admin).getClientByUsername(username);
            if (client != null && client.getPassword().equals(password)) {
                openClientView(client);
            } else if (client != null) {
                System.err.println("Wrong password, please try again.");
                printLoginPrompt();
            } else {
                System.err.println("No account found with that info");
                accountCreationChoice();
            }
        }
    }

    private void accountCreationChoice() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Create an account?");
        System.out.println("(1) -> Yes\n(2) -> No");

        int choice;
        do {
            choice = scanner.nextInt();
            if (choice == 1) {
                accountCreationPrompts();
                System.out.println("Account created!");
                printInitialPrompt();
                break;
            } else {
                System.err.println("Invalid choice!");
            }
        } while (choice != 2);
    }

    private void accountCreationPrompts() {
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();

        try {
            System.out.println("Enter a username: ");
            String username = scanner.nextLine();

            System.out.println("Enter a password: ");
            String password;
            if (console != null) {
                char[] passwordChars = console.readPassword();
                password = new String(passwordChars);
            } else {
                password = scanner.nextLine();
            }

            System.out.println("Enter birth date (YYYY-MM-DD): ");
            String birthDateString = scanner.nextLine();
            LocalDate birthDate = LocalDate.parse(birthDateString);

            System.out.println("Enter an e-mail address: ");
            String eMail = scanner.nextLine();

            Client client = new Client(null, username, password, birthDate, eMail);
            ClientRegistrant registrant = new ClientRegistrant(client);
            registrant.registerUser();

        } catch (IllegalArgumentException | DateTimeException e) {
            LOGGER.warning(e.getMessage());
            System.err.println(e.getMessage());
            accountCreationPrompts();
        }
    }
    private void openClientView(Client client){
        System.out.println("Welcome, "+client.getUsername()+'!');
        ClientController clientController = new ClientController(client.getEmail());
        Dashboard clientDashboard = new ClientDashboard(clientController);
        clientDashboard.getOptions();
    }

    private void openAdminView(){
        System.out.println("Welcome, overlord!");
        AdminController adminController = new AdminController(admin);
        Dashboard adminDashboard = new AdminDashboard(adminController);
        adminDashboard.getOptions();
    }
}