package entries;

import entries.util.EntryValidator;
import exceptions.InvalidEmailInputException;
import exceptions.InvalidNameInputException;

import java.io.Serializable;
import java.time.LocalDate;


/* (almost) pojo class to store entry data */
public class Entry implements Serializable {

    private final String firstName;
    private final String lastName;
    private final String eMail;
    private final LocalDate birthDate;


    /*Builder class for creating Entry objects...
     just for show, not an optimal choice by any means.*/
    public static class Builder {

        private String firstName;
        private String lastName;
        private String eMail;
        private LocalDate birthDate;

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        public Builder setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }
        public Builder setEMail(String eMail) {
            this.eMail = eMail;
            return this;}
        public Entry build(){
            return new Entry(this);
        }
    }

    private Entry(Builder builder) {
        if (!EntryValidator.isNameValid(builder.firstName) || !EntryValidator.isNameValid(builder.lastName)) {
            throw new InvalidNameInputException();
        }
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;

        if (!EntryValidator.isEmailValid(builder.eMail)) {
            throw new InvalidEmailInputException();
        }
        this.eMail = builder.eMail;

        if (!EntryValidator.isDateValid(builder.birthDate)) {
            throw new IllegalArgumentException("Date cannot be in the future!");
        }
        this.birthDate = builder.birthDate;
    }

    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getEMail() {return eMail;}
    public LocalDate getBirthDate() {return birthDate;}

    @Override
    public String toString() {
        return "|" + " " +
                firstName + " " +
                lastName + " | " +
                birthDate + " | " +
                eMail +" "+ '|';
    }
}
