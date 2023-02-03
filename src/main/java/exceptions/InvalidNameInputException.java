package exceptions;

public class InvalidNameInputException extends RuntimeException{

    public String getMessage(){
        return "Invalid name!";
    }
}
