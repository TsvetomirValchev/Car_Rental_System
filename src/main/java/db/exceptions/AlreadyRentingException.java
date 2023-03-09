package db.exceptions;

public class AlreadyRentingException extends RuntimeException {

    @Override
    public String getMessage(){
        return "You are already renting a car!";
    }
}
