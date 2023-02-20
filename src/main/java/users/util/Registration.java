package users.util;

import db.AdminController;
import db.ClientController;
import users.Client;

import java.time.LocalDate;

public class Registration extends UserValidator {

    private static final AdminController adminController = new AdminController();

    public static void registerUser(String username, String password, LocalDate birthDate, String eMail) {
        if(!doesUserExist(username, password)) {
            validateUser(username, password, eMail, birthDate);
            buildClient(username, password, birthDate, eMail);
        }
    }
    private static void validateUser(String username, String password, String eMail, LocalDate birthDate) {
        if(!isNameValid(username)) {
            throw new IllegalArgumentException("Invalid username!");
        }
        if(doesUsernameExist(username)){
            throw new IllegalArgumentException("Username is already taken!");
        }
        if(!isPasswordValid(password)) {
            throw new IllegalArgumentException("Invalid password!");
        }
        if(!isEmailValid(eMail)) {
            throw new IllegalArgumentException("Invalid e-mail address!");
        }
        if(isEmailTaken(eMail)){
            throw new IllegalArgumentException("Account with that e-mail already exists!");
        }
        if(!isDateValid(birthDate)) {
            throw new IllegalArgumentException("User must be over 18 years old!");
        }
    }
    private static void buildClient(String username, String password, LocalDate birthDate, String eMail) {
        Client client = new Client(username, password, birthDate, eMail);
        adminController.addClient(client);
    }

    public static boolean doesUserExist(String username, String password) {
        return doesUsernameExist(username) && doesPasswordMatch(username, password);
    }

    public static boolean doesUsernameExist(String username) {
        for(Client c: ClientController.readClients().values()) {
            if(c.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private static boolean doesPasswordMatch(String username, String password) {
        for(Client c: ClientController.readClients().values()) {
            if(c.getUsername().equals(username) && c.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isEmailTaken(String eMail) {
        for(Client c: ClientController.readClients().values()) {
            if(c.getEMail().equals(eMail)) {
                return true;
            }
        }
        return false;
    }

}
