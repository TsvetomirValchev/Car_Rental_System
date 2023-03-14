package users.util;

import db.AdminController;
import users.Admin;
import users.Client;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class UserValidator{

    private static final String NAME_PATTERN = "^[a-zA-Z0-9]{3,20}$";
    private static final String EMAIL_PATTERN = ".+@.+..+";
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    private static final AdminController adminController = new AdminController(new Admin());

    private final Client client;

    public UserValidator(Client client) {
        this.client = client;
    }

    void validateUser(){
        if(!isNameValid()) {
            throw new IllegalArgumentException("Invalid username!");
        }
        if(doesUsernameExist()){
            throw new IllegalArgumentException("Username is already taken!");
        }
        if(!isPasswordValid()) {
            throw new IllegalArgumentException("Invalid password!");
        }
        if(!isEmailValid()) {
            throw new IllegalArgumentException("Invalid e-mail address!");
        }
        if(isEmailTaken()){
            throw new IllegalArgumentException("Account with that e-mail already exists!");
        }
        if(!isDateValid()) {
            throw new IllegalArgumentException("User must be over 18 years old!");
        }
    }

    public boolean isNameValid(){
        if(client.getUsername().equals(new Admin().getUsername()) || client.getUsername().contains("admin")){
            return false;
        }
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        return pattern.matcher(client.getUsername()).matches();
    }

    public boolean isEmailValid(){
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(client.getEmail()).matches();
    }

    public boolean isDateValid(){
        return !client.getBirthDate().isAfter(LocalDate.now().minusYears(18));
    }

    public boolean isPasswordValid(){
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        return pattern.matcher(client.getPassword()).matches();
    }

    private boolean isEmailTaken() {
        for(Client c: adminController.getAllClients().values()) {
            if(c.getEmail().equals(client.getEmail())) {
                return true;
            }
        }
        return false;
    }

    boolean doesUserExist() {
        return doesUsernameExist() && doesPasswordMatch();
    }
    private boolean doesUsernameExist() {
        for(Client c: adminController.getAllClients().values()) {
            if(c.getUsername().equals(client.getUsername())) {
                return true;
            }
        }
        return false;
    }
    private boolean doesPasswordMatch() {
        for(Client c: adminController.getAllClients().values()) {
            if(c.getUsername().equals(client.getUsername()) && c.getPassword().equals(client.getPassword())) {
                return true;
            }
        }
        return false;
    }
}

