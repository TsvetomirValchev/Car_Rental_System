package db;

import pojos.Person;
import logging.LoggerManager;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;


public class PersonDOA extends EntryDOA<Person> {

    private static final Logger LOGGER = LoggerManager.getLogger(PersonDOA.class.getName());

    public PersonDOA(String tablePrimaryKey) {
        super("entries", tablePrimaryKey);
    }

    @Override
    public String buildCreationQuery(Object object) {
        return "INSERT INTO "
                + this.tableName +
                "(first_name, last_name, birth_date, email_address) VALUES(?, ?, ?, ?)";
    }

    @Override
    protected Person mapReadResultSetToObject(ResultSet resultSet) throws SQLException {
        return new Person(
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getDate("birth_date").toLocalDate(),
                resultSet.getString(this.tablePrimaryKey)
        );
    }
    @Override
    protected String getKey(Person object) {
        return object.getEMail();
    }

    @Override
    protected void setUpdatedValues(PreparedStatement statement, int propertyIndex, String updatedValue) {
        try{
            switch (propertyIndex) {
                case 1, 2, 4 -> statement.setString(1, updatedValue);
                case 3 -> statement.setDate(1, Date.valueOf(LocalDate.parse(updatedValue)));
            }
        } catch (SQLException e) {
            LOGGER.warning(e+" Error updating person!");
        }
    }
    @Override
    protected String buildUpdateQuery(int propertyIndex) {
        Map<Integer, String> columnMap = Map.of(
                1, this.tablePrimaryKey,
                2, "last_name",
                3, "birth_date",
                4, "email_address"
        );
        String columnName = columnMap.get(propertyIndex);
        return "UPDATE "+ this.tableName +" SET " + columnName + "=? WHERE "+this.tablePrimaryKey+ "=?";
    }
}