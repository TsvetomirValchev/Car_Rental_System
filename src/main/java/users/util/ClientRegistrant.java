package users.util;

import db.AdminController;
import users.Admin;
import users.Client;

public class ClientRegistrant {

    private final Client client;

    public ClientRegistrant(Client client) {
        this.client = client;
    }

    public void registerUser() {
        UserValidator userValidator = new UserValidator(client);
        if(!userValidator.doesUserExist()) {
            new UserValidator(client).validateUser();
            buildClient();
        }
    }

    private void buildClient() {
        new AdminController(Admin.getInstance()).addClient(client);
    }
}
