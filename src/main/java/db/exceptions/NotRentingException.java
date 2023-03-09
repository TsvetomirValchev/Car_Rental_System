package db.exceptions;

public class NotRentingException extends RuntimeException{

    @Override
    public String getMessage(){
        return "You are not currently renting a car!";
    }
}
