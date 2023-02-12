package pojos;

import db.CarDOA;
import db.util.CarManager;
import logging.LoggerManager;
import pojos.util.EntryValidator;

import java.time.LocalDate;
import java.util.logging.Logger;

public class Person {

    private static final Logger LOGGER = LoggerManager.getLogger(Person.class.getName());
    private static final CarDOA carDOA = new CarDOA("car_id");

    private String firstName;
    private String lastName;
    private String eMail;
    private LocalDate birthDate;

    public Person(String firstName, String lastName, LocalDate birthDate, String eMail) {
       try{
           if (!EntryValidator.isNameValid(firstName) || !EntryValidator.isNameValid(lastName)) {
               throw new IllegalArgumentException("Invalid name input!");
           }
           this.firstName = firstName;
           this.lastName = lastName;

           if (!EntryValidator.isEmailValid(eMail)) {
               throw new IllegalArgumentException("Invalid e-mail input!");
           }
           this.eMail = eMail;

           if (!EntryValidator.isDateValid(birthDate)) {
               throw new IllegalArgumentException("Date cannot be in the future!");
           }
           this.birthDate = birthDate;
       }catch (IllegalArgumentException e){
           LOGGER.finest(e.toString());
       }
    }

    public void rentCar(Car car) {
        updateCarRentalStatus(car.getId(), true);
    }
    public void returnCar(Car car) {
        updateCarRentalStatus(car.getId(), false);
    }
    private void updateCarRentalStatus(String carId, boolean isRent) {
        try {
            if (isRent == CarManager.isCarFreeToRent(carId)) {
                carDOA.update(carId, 4, isRent ? this.getEMail() : null);
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
            if (isRent) {
                LOGGER.finest("Car is already taken!");
            } else {
                LOGGER.finest("Car was never rented!");
            }
        }
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
