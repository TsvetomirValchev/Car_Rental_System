package db;

import cars.Trip;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

public class TripDAO extends EntryDAO<Trip> {

    public TripDAO(){
        super("rental_history", "id");
    }

    @Override
    protected String buildCreationQuery(Object object) {
        return "INSERT INTO " + tableName +
                "(client_id, car_id, rent_time, return_time) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected void setValues(PreparedStatement statement, Object object) throws SQLException {
        if (object instanceof Trip trip) {
            statement.setInt(1, trip.getClientId());
            statement.setInt(2, trip.getCarId());
            statement.setTimestamp(3, Timestamp.valueOf(trip.getRentTime()));
            if (trip.getReturnTime().isPresent()) {
                statement.setTimestamp(4, Timestamp.valueOf(trip.getReturnTime().get()));
            } else {
                statement.setNull(4, Types.TIMESTAMP);
            }
        }
    }

    @Override
    protected Trip mapReadResultSetToObject(ResultSet resultSet) throws SQLException {
        LocalDateTime returnTime = null;
        Timestamp returnTimestamp = resultSet.getTimestamp("return_time");
        if (returnTimestamp != null) {
            returnTime = returnTimestamp.toLocalDateTime();
        }
        return new Trip(
                resultSet.getInt("id"),
                resultSet.getInt("client_id"),
                resultSet.getInt("car_id"),
                resultSet.getTimestamp("rent_time").toLocalDateTime(),
                Optional.ofNullable(returnTime)
        );
    }

    @Override
    protected String getKey(Trip object) {
        return Integer.toString(object.getId());
    }

    @Override
    protected void setUpdatedValues(PreparedStatement statement, int propertyIndex, Object updatedValue) throws SQLException {
        switch (propertyIndex){
            case 1,2-> statement.setInt(1,(Integer) updatedValue);
            case 3,4 -> statement.setTimestamp(1, Timestamp.valueOf((LocalDateTime) updatedValue));
        }
    }
    @Override
    protected String buildUpdateQuery(int propertyIndex) {
        Map<Integer, String> columnMap = Map.of(
                1, "client_id",
                2, "car_id",
                3, "rent_time",
                4, "return_time"
        );
        String columnName = columnMap.get(propertyIndex);
        return "UPDATE " + tableName + " SET " + columnName + "=? WHERE " + tablePrimaryKey + "=?";
    }
}