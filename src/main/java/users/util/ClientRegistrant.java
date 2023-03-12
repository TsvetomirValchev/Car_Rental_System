package users.util;

import db.AdminController;
import db.interfaces.ExceptionTransmitter;
import logging.LoggerManager;
import users.Admin;
import users.Client;
import view.LogInMenu;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientRegistrant implements ExceptionTransmitter {

    private static final Logger LOGGER = LoggerManager.getLogger(ClientRegistrant.class.getName());
    private final LogInMenu logInMenu = new LogInMenu();

    private final Client client;

    public ClientRegistrant(Client client) {
        this.client = client;
    }

    public void registerUser() {
        try{
            UserValidator userValidator = new UserValidator(client);
            if(!userValidator.doesUserExist()) {
                userValidator.validateUser();
                buildClient();
            }
        }catch (IllegalArgumentException e){
            transmitException(e,Level.WARNING,e.getMessage());
        }
    }

    private void buildClient() {
        new AdminController(new Admin()).addClient(client);
    }

    @Override
    public void transmitException(Exception e, Level severity, String message) {
        logException(e,severity);
        logInMenu.printExceptionMessage(message);
    }

    @Override
    public void logException(Exception e, Level severity) {
        LOGGER.log(severity,e.getMessage());
    }
}