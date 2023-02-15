package users;

import users.util.UserType;

import java.time.LocalDate;

public class Client extends User{

    private final LocalDate birthDate;
    private final String eMail;

    public Client(String username, String password, LocalDate birthDate, String eMail) {
        super(username,password);
        this.eMail=eMail;
        this.birthDate = birthDate;
    }

    @Override
    protected UserType getUserType() {
        return UserType.CLIENT;
    }
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public String getEMail() {
        return eMail;
    }

    @Override
    public String toString() {
        return "|" + " " +
                this.getUsername() + " | " +
                this.getPassword() + " | " +
                this.getBirthDate() + " | " +
                this.getEMail() +" "+ '|';
    }
}
