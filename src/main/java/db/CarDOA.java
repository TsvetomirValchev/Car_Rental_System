package db;

import pojos.Car;
import logging.LoggerManager;

import java.sql.*;
import java.util.Map;
import java.util.logging.Logger;


public class CarDOA extends EntryDOA<Car>{

    private static final Logger LOGGER = LoggerManager.getLogger(CarDOA.class.getName());

    public CarDOA(String tablePrimaryKey) {
        super("cars", tablePrimaryKey);
    }

    @Override
    protected String buildCreationQuery(Object object) {
        return "INSERT INTO "
                + this.tableName+
                "(car_id,make,model,owner_email) VALUES(?, ?, ?,?)";
    }

    @Override
    protected Car mapReadResultSetToObject(ResultSet resultSet) throws SQLException {
        return new Car(
                resultSet.getString(this.tablePrimaryKey),
                resultSet.getString("make"),
                resultSet.getString("model"),
                resultSet.getString("owner_email")
        );
    }
    @Override
    protected String getKey(Car object) {
        return object.getId();
    }

    @Override
    protected void setUpdatedValues(PreparedStatement statement, int propertyIndex, String updatedValue) {
        try{
            statement.setString(1, updatedValue);
        } catch (SQLException e) {
            LOGGER.warning(e+" Error updating car!");
        }
    }
    @Override
    protected String buildUpdateQuery(int propertyIndex) {
        Map<Integer, String> columnMap = Map.of(
                1, this.tablePrimaryKey,
                2, "make",
                3, "model",
                4, "owner_email"
        );
        String columnName = columnMap.get(propertyIndex);
        return "UPDATE "+this.tableName +" SET " + columnName + "=? WHERE "+ this.tablePrimaryKey+" =?";
    }
}
