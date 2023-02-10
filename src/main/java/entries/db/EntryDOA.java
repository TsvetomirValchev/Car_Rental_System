package entries.db;

import entries.Entry;
import logging.LoggerManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

public class EntryDOA {

    private static final Logger LOGGER = LoggerManager.getLogger(EntryDOA.class.getName());
    private String SQL_USERNAME;
    private String SQL_PASSWORD;
    private String SQL_URL;


    EntryDOA() {

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
    }

    private Connection getConnection(){
        try{
            return DriverManager.getConnection(SQL_URL, SQL_USERNAME, SQL_PASSWORD);
        }catch (SQLException e){
            LOGGER.severe(e.toString());
            throw new RuntimeException(e);
        }
    }

    //C
    public void createEntry(Entry entry) {
        String query
                = "INSERT INTO entries(first_name, last_name, birth_date, email_address) VALUES(?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entry.getFirstName());
            statement.setString(2, entry.getLastName());
            statement.setDate(3, java.sql.Date.valueOf(entry.getBirthDate()));
            statement.setString(4, entry.getEMail());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warning(e+" Error creating entry!");
        }
    }

    //R
    public Map<String, Entry> readEntries() {
        String query = "SELECT * FROM entries";
        Map<String, Entry> entries = new HashMap<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Entry entry = new Entry.Builder()
                        .setFirstName(resultSet.getString("first_name"))
                        .setLastName(resultSet.getString("last_name"))
                        .setBirthDate(resultSet.getDate("birth_date").toLocalDate())
                        .setEMail(resultSet.getString("email_address")).build();
                entries.put(entry.getEMail(), entry);
            }
        } catch (SQLException e) {
            LOGGER.warning(e+" Error reading entries!");
        }
        return entries;
    }

    //U
    public void updateEntry(String entryEmail, int propertyIndex, String updatedValue) {
        String query = buildQuery(propertyIndex);

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setStatementValues(statement, propertyIndex, updatedValue);
            statement.setString(2, entryEmail);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warning(e+" Error updating entry!");
        }
    }
    private String buildQuery(int propertyIndex) {
        Map<Integer, String> columnMap = Map.of(
                1, "first_name",
                2, "last_name",
                3, "birth_date",
                4, "email_address"
        );
        String columnName = columnMap.get(propertyIndex);
        return "UPDATE entries SET " + columnName + "=? WHERE email_address=?";
    }
    private void setStatementValues(PreparedStatement statement, int propertyIndex, String updatedValue) {
        try{
            switch (propertyIndex) {
                case 1, 2, 4 -> statement.setString(1, updatedValue);
                case 3 -> statement.setDate(1, Date.valueOf(LocalDate.parse(updatedValue)));
            }
        } catch (SQLException e) {
            LOGGER.warning(e+" Error updating entry!");
        }
    }

    //D
    public void deleteEntry(String email) {
        String query = "DELETE FROM entries WHERE email_address=?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warning(e+" Error deleting entry!");
        }
    }
}
