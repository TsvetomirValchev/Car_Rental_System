package db;

import pojos.Car;
import pojos.Person;
import logging.LoggerManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public abstract class EntryDOA<T> {

    private static final Logger LOGGER = LoggerManager.getLogger(EntryDOA.class.getName());
    private String SQL_USERNAME;
    private String SQL_PASSWORD;
    private String SQL_URL;
    protected final String tableName;
    protected final String tablePrimaryKey;

    protected EntryDOA(String tableName,String tablePrimaryKey){
        try(FileInputStream is = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(is);
            SQL_USERNAME = props.getProperty("SQL_USERNAME");
            SQL_PASSWORD = props.getProperty("SQL_PASSWORD");
            SQL_URL = props.getProperty("SQL_URL");

        } catch (IOException e) {
            LOGGER.severe(e+ "Couldn't load properties!");
            throw new RuntimeException(e);
        }
        this.tableName=tableName;
        this.tablePrimaryKey=tablePrimaryKey;
    }

    protected abstract String buildCreationQuery(Object object);
    protected abstract String getKey(T object);
    protected abstract T mapReadResultSetToObject(ResultSet resultSet) throws SQLException;
    abstract String buildUpdateQuery(int propertyIndex);
    abstract void setUpdatedValues(PreparedStatement statement, int propertyIndex, String updatedValue);

    protected Connection getConnection(){
        try{
            return DriverManager.getConnection(SQL_URL, SQL_USERNAME, SQL_PASSWORD);
        }catch (SQLException e){
            LOGGER.severe(e.toString());
            throw new RuntimeException(e);
        }
    }

    public void create(Object object) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(buildCreationQuery(object))) {
            if (object instanceof Person person) {
                statement.setString(1, person.getFirstName());
                statement.setString(2, person.getLastName());
                statement.setDate(3, java.sql.Date.valueOf(person.getBirthDate()));
                statement.setString(4, person.getEMail());
            } else if (object instanceof Car car) {
                statement.setString(4, car.getId());
                statement.setString(1, car.getMake());
                statement.setString(2, car.getModel());
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warning(e + " Error creating object!");
        }
    }

    public Map<String, T> read() {
        String query = "SELECT * FROM " + this.tableName;
        Map<String, T> entries = new HashMap<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                T object = mapReadResultSetToObject(resultSet);
                entries.put(getKey(object), object);
            }
        } catch (SQLException e) {
            LOGGER.warning(e + " Error reading objects!");
        }
        return entries;
    }

    public void update(String key, int propertyIndex, String updatedValue) {
        String query = buildUpdateQuery(propertyIndex);

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setUpdatedValues(statement, propertyIndex, updatedValue);
            statement.setString(2, key);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warning(e+" Error updating object!");
        }
    }

    public void delete(String key) {
        String query = "DELETE FROM " + this.tableName + " WHERE " + this.tablePrimaryKey + " = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, key);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warning(e+" Error deleting object!");
        }
    }
}


