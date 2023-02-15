package users;

import db.EntryDAO;
import logging.LoggerManager;
import users.util.UserValidator;
import users.util.UserType;

import java.util.logging.Logger;

public abstract class User {

    private static final Logger LOGGER = LoggerManager.getLogger(EntryDAO.class.getName());

    private String username;
    private String password;

    public User(String username, String password) {
        this.username=username;
        this.password=password;
    }

    protected abstract UserType getUserType();

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

}
