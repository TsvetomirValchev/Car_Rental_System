package users;

import java.time.LocalDate;

public class Client extends User {

    private final Integer id;
    private final LocalDate birthDate;
    private final String eMail;

    public Client(Integer id, String username, String password, LocalDate birthDate, String eMail) {
        super(username, password);
        this.id = id;
        this.eMail = eMail;
        this.birthDate = birthDate;
    }

    public Integer getId() {
        return id;
    }
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public String getEmail() {
        return eMail;
    }

    @Override
    public String toString() {
        return "|" + " " +
                getId() + " | " +
                getUsername() + " | " +
                getPassword() + " | " +
                getBirthDate() + " | " +
                getEmail() + " " + '|';
    }
}
