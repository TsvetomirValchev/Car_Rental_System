package db;

import users.Client;

import java.sql.*;
import java.time.LocalDate;
import java.util.Map;

public class ClientDAO extends EntryDAO<Client> {

    public ClientDAO() {
        super("clients", "email");
    }

    @Override
    protected String buildCreationQuery(Object object) {
        return "INSERT INTO " + this.tableName +
                "(username, password, birth_date, email) VALUES(?, ?, ?, ?)";
    }

    @Override
    protected void setValues(PreparedStatement statement, Object object) throws SQLException {
        if (object instanceof Client person) {
            statement.setString(1, person.getUsername());
            statement.setString(2, person.getPassword());
            statement.setDate(3, java.sql.Date.valueOf(person.getBirthDate()));
            statement.setString(4, person.getEmail());
        }
    }

    @Override
    protected Client mapReadResultSetToObject(ResultSet resultSet) throws SQLException {
        return new Client(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getDate("birth_date").toLocalDate(),
                resultSet.getString("email"));
    }

    @Override
    protected String getKey(Client object) {
        return object.getEmail();
    }

    @Override
    protected void setUpdatedValues(PreparedStatement statement, int propertyIndex, Object updatedValue) throws SQLException {
        switch (propertyIndex) {
            case 1 -> statement.setString(1, (String) updatedValue);
            case 2 -> statement.setString(2, (String) updatedValue);
            case 3 -> statement.setString(3, (String) updatedValue);
            case 4 -> statement.setDate(4, Date.valueOf(LocalDate.parse((String) updatedValue)));
        }
    }

    @Override
    protected String buildUpdateQuery(int propertyIndex) {
        Map<Integer, String> columnMap = Map.of(
                1, "username",
                2, "password",
                3, "email",
                4, "birth_date"
        );
        String columnName = columnMap.get(propertyIndex);
        return "UPDATE " + this.tableName + " SET " + columnName + "=? WHERE email=?";
    }
}