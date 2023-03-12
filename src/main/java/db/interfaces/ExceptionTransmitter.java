package db.interfaces;

import java.util.logging.Level;

public interface ExceptionTransmitter {

    void transmitException(Exception e,Level severity, String message);
    void logException(Exception e,Level severity);
}
