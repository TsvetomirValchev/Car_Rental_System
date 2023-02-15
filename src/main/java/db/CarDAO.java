package db;

import cars.Car;
import logging.LoggerManager;

import java.sql.*;
import java.util.Map;
import java.util.logging.Logger;


public class CarDAO extends EntryDAO<Car>{

    private static final Logger LOGGER = LoggerManager.getLogger(CarDAO.class.getName());

    public CarDAO() {
        super("cars", "car_id");
    }

    @Override
    protected String buildCreationQuery(Object object) {
        return "INSERT INTO "
                + this.tableName +
                "(car_id, make, model, price_per_hour) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected void setValues(PreparedStatement statement, Object object) throws SQLException {
        if (object instanceof Car car) {
            statement.setString(1, car.getId());
            statement.setString(2, car.getMake());
            statement.setString(3, car.getModel());
            statement.setDouble(4, car.getPricePerHour());
        }
    }

    @Override
    protected Car mapReadResultSetToObject(ResultSet resultSet) {
        try {
            return new Car(
                    resultSet.getString(this.tablePrimaryKey),
                    resultSet.getString("make"),
                    resultSet.getString("model"),
                    resultSet.getString("client_email"),
                    resultSet.getDouble("price_per_hour")
            );
        } catch (SQLException e) {
            LOGGER.warning(e+" Couldn't construct object!");
        }
        return null;
    }

    @Override
    protected String getKey(Car object) {
        return object.getId();
    }

    @Override
    protected void setUpdatedValues(PreparedStatement statement, int propertyIndex, Object updatedValue) {
        try {
            switch (propertyIndex){
                case 1,2,3,4-> statement.setString(1,(String) updatedValue);
                case 5 -> statement.setDouble(1,(Double) updatedValue);
            }
        } catch (SQLException | NumberFormatException e) {
            LOGGER.warning(e + " Error updating car!");
        }
    }
    @Override
    protected String buildUpdateQuery(int propertyIndex) {
        Map<Integer, String> columnMap = Map.of(
                1, this.tablePrimaryKey,
                2, "make",
                3, "model",
                4, "client_email",
                5, "price_per_hour"
        );
        String columnName = columnMap.get(propertyIndex);
        return "UPDATE " + this.tableName + " SET " + columnName + "=? WHERE " + this.tablePrimaryKey + "=?";
    }
}
