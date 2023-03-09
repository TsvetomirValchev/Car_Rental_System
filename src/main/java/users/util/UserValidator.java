package users.util;

import users.Admin;
import users.Client;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class UserValidator{

    private static final String NAME_PATTERN = "^[a-zA-Z0-9]{3,20}$";
    private static final String EMAIL_PATTERN = ".+@.+..+";
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    private static final Admin admin = Admin.getInstance();

    private final Client client;

    public UserValidator(Client client) {
        this.client = client;
    }

    public boolean isNameValid(){
        if(client.getUsername().equals(admin.getUsername())){
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
}

