package logging;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerManager {

    private static FileHandler fileHandler;
    private static final Path logFilePath = Paths.get("errors.log");

    static {
        try {
            fileHandler = new FileHandler(logFilePath.toString(),true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            getLogger(LoggerManager.class.getName()).addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getLogger(String className) {
        Logger logger = Logger.getLogger(className);
        logger.addHandler(fileHandler);
        return logger;
    }
}