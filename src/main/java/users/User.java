package users;

import db.EntryDAO;
import logging.LoggerManager;
import users.util.UserType;

import java.util.logging.Logger;

public abstract class User {

    private final String username;
    private final String password;

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
