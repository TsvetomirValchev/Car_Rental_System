package users;

public class Admin extends User{

    private static final Admin instance = new Admin("admin","admin");

    private Admin(String username, String password) {
        super(username, password);
    }

    public static Admin getInstance(){
        return instance;
    }
}
