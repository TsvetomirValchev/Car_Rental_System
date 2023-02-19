package view;

import db.AdminController;
import db.ClientDAO;
import db.util.ClientController;
import users.Admin;
import users.Client;
import users.util.Registration;

import java.io.Console;
import java.time.LocalDate;
import java.util.Scanner;

public class LogInMenu {

    private static final Admin admin = Admin.getInstance();
    private static final ClientDAO clientDAO = new ClientDAO();

    public static void printLoginPrompt(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome, enter login info: ");
        String[] loginInfo = scanner.nextLine().split(" ");
        String username = loginInfo[0];
        String password = loginInfo[1];

        if(username.equals(admin.getUsername()) && password.equals(admin.getPassword())){
            openAdminView();
        }else{
            if(Registration.doesUserExist(username,password)){
                Client client = clientDAO.getClientByLogin(username,password);
                openClientView(client);
            }else{
                System.out.println("No account found with that info");
                accountCreationChoice();
            }
        }
    }

    private static void accountCreationChoice() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Create an account?");
        System.out.println("(1) -> Yes\n(2) -> No");

        int choice;
        do {
            choice = scanner.nextInt();
            if (choice == 1) {
                accountCreationPrompts();
                System.out.println("Account created!");
                printLoginPrompt();
                break;
            } else {
                System.err.println("Invalid choice!");
            }
        } while (choice != 0);
    }
    private static void accountCreationPrompts(){
       try{
           Scanner scan = new Scanner(System.in);
           Console console = System.console();

           System.out.println("Enter a username: ");
           String username = scan.nextLine();

           System.out.println("Enter a password: ");
           String password;
           if (console != null) {
               char[] passwordChars = console.readPassword();
               password = new String(passwordChars);
           } else {
               password = scan.nextLine();
           }

           System.out.println("Enter birth date (YYYY-MM-DD): ");
           String birthDateString = scan.nextLine();
           LocalDate birthDate = LocalDate.parse(birthDateString);

           System.out.println("Enter an e-mail address: ");
           String eMail = scan.nextLine();

           Registration.registerUser(username,password,birthDate,eMail);
       }catch (IllegalArgumentException e){
           System.err.println(e.getMessage());
           accountCreationPrompts();
       }
    }

    private static void openClientView(Client client){
        System.out.println("Welcome, "+client.getUsername()+'!');
        ClientController clientController = new ClientController(client);
        Dashboard clientDashboard = new ClientDashboard(clientController);
        clientDashboard.printMenu();
        clientDashboard.getOptions();
    }

    private static void openAdminView(){
        System.out.println("Welcome, overlord!");
        AdminController adminController = new AdminController();
        Dashboard adminDashboard = new AdminDashboard(adminController);
        adminDashboard.printMenu();
        adminDashboard.getOptions();
    }
}
