package exceptions;

public class InvalidEmailInputException extends RuntimeException{

    public String getMessage(){
        return "Invalid e-mail!";
    }
}
