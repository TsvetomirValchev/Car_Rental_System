package db.abstractions;

import logging.LoggerManager;
import view.abstractions.Dashboard;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Controller {
    private static final Logger LOGGER = LoggerManager.getLogger(Controller.class.getName());

    protected abstract Dashboard getDashboard();

    public void transmitException(Exception e, Level severity,String message) {
        logException(e,severity,message);
        getDashboard().printExceptionMessage(message);
    }
    public void logException(Exception e,Level severity, String message){
        if(e.getMessage()!=null){
            LOGGER.log(severity,e.getMessage());
        }else{
            LOGGER.log(severity, message);
        }
    }
}