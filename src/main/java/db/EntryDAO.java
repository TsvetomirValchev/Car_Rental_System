package db;

import logging.LoggerManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public abstract class EntryDAO<T> {

    private static final Logger LOGGER = LoggerManager.getLogger(EntryDAO.class.getName());
    private String SQL_USERNAME;
    private String SQL_PASSWORD;
    private String SQL_URL;
    protected final String tableName;
    protected final String tablePrimaryKey;

    protected EntryDAO(String tableName,String tablePrimaryKey){
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
    protected abstract void setValues(PreparedStatement statement, Object object) throws SQLException;
    protected abstract Object getKey(T object);
    protected abstract T mapReadResultSetToObject(ResultSet resultSet) throws SQLException;
    abstract String buildUpdateQuery(int propertyIndex);
    abstract void setUpdatedValues(PreparedStatement statement, int propertyIndex, Object updatedValue) throws SQLException;

    Connection getConnection() throws SQLException {
        try{
            return DriverManager.getConnection(SQL_URL, SQL_USERNAME, SQL_PASSWORD);
        }catch (SQLException e){
            LOGGER.severe(e.toString());
            throw e;
        }
    }

    protected void create(Object object) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(buildCreationQuery(object))) {
            setValues(statement, object);
            statement.executeUpdate();
        }
    }

    protected Map<Object, T> read() throws SQLException {
        String query = "SELECT * FROM " + this.tableName;
        Map<Object, T> entries = new HashMap<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                T object = mapReadResultSetToObject(resultSet);
                entries.put(getKey(object), object);
            }
        }
        return entries;
    }

    protected void update(Object key, int propertyIndex, Object updatedValue) throws SQLException {
        String query = buildUpdateQuery(propertyIndex);

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setUpdatedValues(statement, propertyIndex, updatedValue);
            statement.setObject(2, key);
            statement.executeUpdate();
        }
    }

    protected void delete(Object key) throws SQLException {
        String query = "DELETE FROM " + this.tableName + " WHERE " + this.tablePrimaryKey + " = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, key);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLDataException("Entry with key " + key + " was not found!");
            }
        }
    }

}