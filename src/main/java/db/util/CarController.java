package db.util;

import db.CarDAO;
import db.ClientDAO;
import cars.Car;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CarController{

    private static final CarDAO carDAO = new CarDAO();

    private final Car carModel;

    public CarController(Car carModel){
        this.carModel=carModel;
    }

    public boolean isCarFreeToRent(){
       return this.carModel.getClientEmail() == null;
    }

    public static List<Car> getFreeCars(){
        return new ArrayList<>(carDAO.read().values())
                .stream()
                .filter(car -> car.getClientEmail() == null)
                .sorted(Comparator.comparing(Car::getMake))
                .toList();
    }
}
