package users.util;

import db.AdminController;
import users.Client;

public class ClientRegistrant {

    private static final AdminController adminController = new AdminController();

    private final Client client;

    public ClientRegistrant(Client client) {
        this.client = client;
    }

    public void registerUser() {
        if(!doesUserExist()) {
            validateUser();
            buildClient();
        }
    }
    private void validateUser(){
        UserValidator validator = new UserValidator(client);
        if(!validator.isNameValid()) {
            throw new IllegalArgumentException("Invalid username!");
        }
        if(doesUsernameExist()){
            throw new IllegalArgumentException("Username is already taken!");
        }
        if(!validator.isPasswordValid()) {
            throw new IllegalArgumentException("Invalid password!");
        }
        if(!validator.isEmailValid()) {
            throw new IllegalArgumentException("Invalid e-mail address!");
        }
        if(isEmailTaken()){
            throw new IllegalArgumentException("Account with that e-mail already exists!");
        }
        if(!validator.isDateValid()) {
            throw new IllegalArgumentException("User must be over 18 years old!");
        }
    }

    private void buildClient() {
        adminController.addClient(client);
    }

    private boolean doesUserExist() {
        return doesUsernameExist() && doesPasswordMatch();
    }
    private boolean doesUsernameExist() {
        for(Client c: adminController.readAllClients().values()) {
            if(c.getUsername().equals(client.getUsername())) {
                return true;
            }
        }
        return false;
    }
    private boolean doesPasswordMatch() {
        for(Client c: adminController.readAllClients().values()) {
            if(c.getUsername().equals(client.getUsername()) && c.getPassword().equals(client.getPassword())) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmailTaken() {
        for(Client c: adminController.readAllClients().values()) {
            if(c.getEmail().equals(client.getEmail())) {
                return true;
            }
        }
        return false;
    }
}
