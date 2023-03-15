package db;

import cars.Car;
import cars.RentalCar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CarDAO extends EntryDAO<RentalCar>{

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
        if (object instanceof RentalCar rentalCar) {
            statement.setObject(1, rentalCar.getId());
            statement.setString(2, rentalCar.getMake());
            statement.setString(3, rentalCar.getModel());
            statement.setDouble(4, rentalCar.getPricePerHour());
            statement.setObject(5, rentalCar.getClientId());
        }
    }

    @Override
    protected RentalCar mapReadResultSetToObject(ResultSet resultSet) throws SQLException {
        return new RentalCar(
                resultSet.getInt(this.tablePrimaryKey),
                resultSet.getString("make"),
                resultSet.getString("model"),
                resultSet.getDouble("price_per_hour"),
                resultSet.getInt("client_id"),
                resultSet.getBoolean("is_free")
        );
    }

    @Override
    protected Integer getKey(RentalCar object) {
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

    /*Models and brands util*/

    protected List<Car> readCarBrands() throws SQLException {
        String query = "SELECT DISTINCT make FROM carbrands";
        List<Car> brands = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String make = resultSet.getString("make");
                brands.add(new Car(make, null));
            }
        }

        return brands;
    }

    protected List<Car> readCarModels(String brand) throws SQLException {
        String query = "SELECT DISTINCT model FROM carmodels WHERE make = ?";
        List<Car> models = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, brand);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String model = resultSet.getString("model");
                models.add(new Car(brand, model));
            }
        }
        return models;
    }
}
