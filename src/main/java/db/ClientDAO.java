package db;

import users.Client;
import logging.LoggerManager;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;


public class ClientDAO extends EntryDAO<Client> {

    private static final Logger LOGGER = LoggerManager.getLogger(ClientDAO.class.getName());

    public ClientDAO() {
        super("entries", "email_address");
    }

    @Override
    protected String buildCreationQuery(Object object) {
        return "INSERT INTO "
                + this.tableName +
                "(username, password, birth_date, email_address) VALUES(?, ?, ?, ?)";
    }
    @Override
    protected void setValues(PreparedStatement statement, Object object) throws SQLException {
        if (object instanceof Client person) {
            statement.setString(1, person.getUsername());
            statement.setString(2, person.getPassword());
            statement.setDate(3, java.sql.Date.valueOf(person.getBirthDate()));
            statement.setString(4, person.getEMail());
        }
    }

    @Override
    protected Client mapReadResultSetToObject(ResultSet resultSet) {
        try {
            return new Client(
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getDate("birth_date").toLocalDate(),
                    resultSet.getString(this.tablePrimaryKey)
            );
        } catch (SQLException e) {
            LOGGER.warning(e+" Couldn't construct object!");
        }
        return null;
    }
    @Override
    protected String getKey(Client object) {
        return object.getEMail();
    }

    @Override
    protected void setUpdatedValues(PreparedStatement statement, int propertyIndex, Object updatedValue) {
        try{
            switch (propertyIndex) {
                case 1, 2, 4 -> statement.setString(1, (String) updatedValue);
                case 3 -> statement.setDate(1, Date.valueOf(LocalDate.parse((String)updatedValue)));
            }
        } catch (SQLException e) {
            LOGGER.warning(e+" Error updating person!");
        }
    }
    @Override
    protected String buildUpdateQuery(int propertyIndex) {
        Map<Integer, String> columnMap = Map.of(
                1,"username",
                2, "password",
                3, "birth_date",
                4,  this.tablePrimaryKey
        );
        String columnName = columnMap.get(propertyIndex);
        return "UPDATE "+ this.tableName +" SET " + columnName + "=? WHERE "+this.tablePrimaryKey+ "=?";
    }

    public Client getClientByLogin(String username, String password){
        for(Client client: read().values()){
            if(client.getUsername().equals(username) && client.getPassword().equals(password)){
                return client;
            }
        }
        return null;
    }
}