package users.util;

import db.AdminController;
import db.abstractions.Controller;
import users.Admin;
import users.Client;
import view.LogInDashboard;
import view.abstractions.Dashboard;

import java.util.logging.Level;

public class ClientRegistrant extends Controller {
    private final Client client;
    private final LogInDashboard logInDashboard = new LogInDashboard();

    public ClientRegistrant(Client client) {
        this.client = client;
    }

    @Override
    protected Dashboard getDashboard() {
        return logInDashboard;
    }

    public void registerUser() {
        try {
            UserValidator userValidator = new UserValidator(client);
            if (!userValidator.doesUserExist()) {
                userValidator.validateUser();
                buildClient();
            }
        } catch (IllegalArgumentException e) {
            transmitException(e, Level.WARNING, e.getMessage());
        }
    }

    private void buildClient() {
        new AdminController(new Admin()).addClient(client);
    }
}