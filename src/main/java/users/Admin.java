package users;

import users.util.UserType;

public class Admin extends User{

    private static final Admin instance = new Admin("admin","admin");

    private Admin(String username, String password) {
        super(username, password);
    }

    public static Admin getInstance(){
        return instance;
    }

    @Override
    protected UserType getUserType() {
        return UserType.ADMIN;
    }
}
