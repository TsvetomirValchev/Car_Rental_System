package pojos.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class EntryValidator {

    private static final String NAME_PATTERN = "^[A-Z][a-z]+$";
    private static final String EMAIL_PATTERN = ".+@.+..+";

    private static final String CAR_ID_PATTERN = "^[A-Z]{2}[0-9]{4}[A-Z]{2}$";


    public static boolean isNameValid(String firstNameOrLastName){
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        return pattern.matcher(firstNameOrLastName).matches();
    }

    public static boolean isEmailValid(String eMail){
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(eMail).matches();
    }

    public static boolean isDateValid(LocalDate birthDate){
        LocalDate eighteenYearsAgo = LocalDate.now().minusYears(18);
        return !birthDate.isAfter(eighteenYearsAgo);
    }

    public static boolean isCarIDValid(String carId){
        Pattern pattern = Pattern.compile(CAR_ID_PATTERN);
        return pattern.matcher(carId).matches();
    }
}
