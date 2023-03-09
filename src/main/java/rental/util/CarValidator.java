package rental.util;

import java.util.List;
import java.util.regex.Pattern;

public class CarValidator {
    private static final String CAR_ID_PATTERN = "^[A-Z]{2}[0-9]{4}[A-Z]{2}$";

    public static boolean isCarIDValid(String carId){
        Pattern pattern = Pattern.compile(CAR_ID_PATTERN);
        return pattern.matcher(carId).matches();
    }

    public static boolean isCarBrandValid(String make){
        List<String> existingBrands = List.of(""); //assume there is a resource with brands
        return existingBrands.contains(make);
    }
}
