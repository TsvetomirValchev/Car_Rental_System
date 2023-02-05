package entries.util;

import java.util.regex.Pattern;

public class EntryValidator {

    private static final String NAME_PATTERN = "^[A-Z][a-z]+$";
    private static final String EMAIL_PATTERN = ".+@.+..+";


    public static boolean isNameValid(String firstNameOrLastName){
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        return pattern.matcher(firstNameOrLastName).matches();
    }

    public static boolean isEmailValid(String eMail){
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(eMail).matches();
    }
}
