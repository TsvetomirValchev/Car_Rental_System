package db;

import rental.Car;

import java.sql.*;
import java.util.Map;


public class CarDAO extends EntryDAO<Car>{

    public CarDAO(){
        super("cars", "car_id");
    }

    @Override
    protected String buildCreationQuery(Object object) {
        return "INSERT INTO " + this.tableName +
                "(car_id, make, model, price_per_hour, client_id) " +
                "VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected void setValues(PreparedStatement statement, Object object) throws SQLException {
        if (object instanceof Car car) {
            statement.setObject(1, car.getId());
            statement.setString(2, car.getMake());
            statement.setString(3, car.getModel());
            statement.setDouble(4, car.getPricePerHour());
            statement.setObject(5, car.getClientId());
        }
    }

    @Override
    protected Car mapReadResultSetToObject(ResultSet resultSet) throws SQLException {
        return new Car(
                resultSet.getInt(this.tablePrimaryKey),
                resultSet.getString("make"),
                resultSet.getString("model"),
                resultSet.getDouble("price_per_hour"),
                resultSet.getInt("client_id"),
                resultSet.getBoolean("is_free")
        );
    }

    @Override
    protected Integer getKey(Car object) {
        return object.getId();
    }

    @Override
    protected void setUpdatedValues(PreparedStatement statement, int propertyIndex, Object updatedValue) throws SQLException {
        switch (propertyIndex) {
            case 2,3 -> statement.setString(1, (String) updatedValue);
            case 4 -> statement.setDouble(1, (Double) updatedValue);
            case 1,5 -> statement.setObject(1, updatedValue);
        }
    }
    @Override
    protected String buildUpdateQuery(int propertyIndex) {
        Map<Integer, String> columnMap = Map.of(
                1, this.tablePrimaryKey,
                2, "make",
                3, "model",
                4, "price_per_hour",
                5, "client_id"
        );
        String columnName = columnMap.get(propertyIndex);
        return "UPDATE " + this.tableName + " SET " + columnName + "=? WHERE " + this.tablePrimaryKey + "=?";
    }
}
