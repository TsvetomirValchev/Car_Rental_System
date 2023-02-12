package pojos;

import logging.LoggerManager;
import pojos.util.EntryValidator;

import java.util.logging.Logger;

public class Car {

    private static final Logger LOGGER = LoggerManager.getLogger(Car.class.getName());

    private String id;
    private String make;
    private String model;
    private String ownerEmail;

    public Car(String id, String make, String model, String ownerEmail) {
      try{
          if(EntryValidator.isCarIDValid(id)){
              this.id = id;
          }else{
              throw new IllegalArgumentException();
          }
          this.make = make;
          this.model = model;
          this.ownerEmail = ownerEmail;
      }catch (IllegalArgumentException e){
          LOGGER.fine("Illegal car license plate! "+e);
      }
    }

    public String getId() {
        return id;
    }
    public String getMake() {
        return make;
    }
    public String getModel() {
        return model;
    }
    public String getOwnerEmail() {
        return ownerEmail;
    }

    @Override
    public String toString() {
        return "|" + " " +
                id + " | " +
                make + " | " +
                model + " | "+
                ownerEmail;
    }
}

