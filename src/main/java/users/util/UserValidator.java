package users.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class UserValidator {

    private static final String NAME_PATTERN = "^[A-Z][a-z]+$";
    private static final String EMAIL_PATTERN = ".+@.+..+";
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    protected static boolean isNameValid(String firstNameOrLastName){
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        return pattern.matcher(firstNameOrLastName).matches();
    }

    protected static boolean isEmailValid(String eMail){
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(eMail).matches();
    }

    protected static boolean isDateValid(LocalDate birthDate){
        LocalDate eighteenYearsAgo = LocalDate.now().minusYears(18);
        return !birthDate.isAfter(eighteenYearsAgo);
    }

    protected static boolean isPasswordValid(String password){
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        return pattern.matcher(password).matches();
    }
}

