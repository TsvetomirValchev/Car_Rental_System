package users.util;

import db.AdminController;
import db.ClientDAO;
import db.EntryDAO;
import logging.LoggerManager;
import users.Client;

import java.time.LocalDate;
import java.util.logging.Logger;

public class Registration extends UserValidator {

    private static final Logger LOGGER = LoggerManager.getLogger(UserValidator.class.getName());
    private static final AdminController adminController = new AdminController();
    private static final EntryDAO<Client> clientDAO = new ClientDAO();

    public static void registerUser(String username, String password, LocalDate birthDate, String eMail) {
        try {
            if(!doesUserExist(username, password)) {
                validateUser(username, password, eMail, birthDate);
                addClient(username, password, birthDate, eMail);
            }
        } catch (IllegalArgumentException e) {
            LOGGER.finest(e.toString());
        }
    }
    private static void validateUser(String username, String password, String eMail, LocalDate birthDate) {
        if(!isNameValid(username)) {
            throw new IllegalArgumentException("Invalid username!");
        }
        if(!isPasswordValid(password)) {
            throw new IllegalArgumentException("Invalid password!");
        }
        if(!isEmailValid(eMail)) {
            throw new IllegalArgumentException("Invalid e-mail address!");
        }
        if(!isDateValid(birthDate)) {
            throw new IllegalArgumentException("User must be over 18 years old!");
        }
    }
    private static void addClient(String username, String password, LocalDate birthDate, String eMail) {
        Client client = new Client(username, password, birthDate, eMail);
        adminController.addClient(client);
    }

    public static boolean doesUserExist(String username, String password) {
        for(Client c: clientDAO.read().values()) {
            if(c.getUsername().equals(username) && c.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
