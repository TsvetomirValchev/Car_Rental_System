package db.util;

import db.CarDOA;
import db.PersonDOA;
import pojos.Car;
import pojos.Person;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class CarManager{
    private static final CarDOA carDOA = new CarDOA("car_id");
    private static final PersonDOA personDOA = new PersonDOA("email_address");

    private CarManager(){}

    /*TODO
        Various access and sorting methods*/


    public static boolean isCarFreeToRent(String carId){
        List<Car> cars = new ArrayList<>(carDOA.read().values());
        for (Car car : cars) {
            if (car.getId().equals(carId)) {
                return car.getOwnerEmail() == null;
            }
        }
        return false;
    }

    public static List<Car> getFreeCars(){
        return new ArrayList<>(carDOA.read().values())
                .stream()
                .filter(car -> car.getOwnerEmail() == null)
                .sorted(Comparator.comparing(Car::getMake))
                .toList();
    }

    public static List<Person> getPeopleRentingCars(){
        List<Car> carList = new ArrayList<>(carDOA.read().values());
        return new ArrayList<>(personDOA.read().values())
                .stream()
                .filter(person -> carList.stream()
                        .filter(car -> car.getOwnerEmail() != null)
                        .anyMatch(car -> car.getOwnerEmail().equals(person.getEMail())))
                .toList();
    }











}
