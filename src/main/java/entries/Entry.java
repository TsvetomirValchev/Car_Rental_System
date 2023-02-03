package entries;

import entries.util.EntryValidator;
import exceptions.InvalidEmailInputException;
import exceptions.InvalidNameInputException;

import java.io.Serializable;
import java.time.LocalDate;


/* (almost) pojo class to store entry data */
public class Entry implements Serializable {

    private String firstName;
    private String lastName;
    private String eMail;
    private LocalDate birthDate;


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
        this.birthDate =builder.birthDate;
        try {
            if (!EntryValidator.isNameValid(builder.firstName)) {
                throw new InvalidNameInputException();
            }
            this.firstName = builder.firstName;

            if (!EntryValidator.isNameValid(builder.lastName)) {
                throw new InvalidNameInputException();
            }
            this.lastName = builder.lastName;
        } catch (InvalidNameInputException e) {
            e.printStackTrace();
        }
        try{
            if(EntryValidator.isEmailValid(builder.eMail)){
                this.eMail = builder.eMail;
            } else throw new InvalidEmailInputException();
        }catch (InvalidEmailInputException e){
            e.printStackTrace();
        }
    }

    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setEMail(String eMail) {this.eMail = eMail;}
    public void setBirthDate(LocalDate birthDate) {this.birthDate = birthDate;}

    /* only for testing purposes */
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getEMail() {return eMail;}
    public LocalDate getBirthDate() {return birthDate;}

    @Override
    public String toString() {
        return "|" + " " +
                firstName + " " +
                lastName + " | "+
                birthDate + " | " +
                eMail +" "+ '|';
    }
}
