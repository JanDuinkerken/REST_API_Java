package es.udc.ws.app.model.register;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlRegisterDao extends AbstractSqlRegisterDao {

    @Override
    public Register addRegister(Connection connection, Register register) {

        String queryString = "INSERT INTO Register"
                + " (tripId, email, numReserves, creditCard, price, registerDate)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            int i = 1;
            preparedStatement.setLong(i++, register.getTripId());
            preparedStatement.setString(i++, register.getEmail());
            preparedStatement.setInt(i++, register.getNumReserves());
            preparedStatement.setString(i++, register.getCreditCard());
            preparedStatement.setFloat(i++, register.getPrice());
            preparedStatement.setTimestamp(i, Timestamp.valueOf(register.getRegisterDate()));

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long registerId = resultSet.getLong(1);

            return new Register(registerId, register.getTripId(), register.getEmail(), register.getNumReserves(),
                    register.getCreditCard(), register.getPrice(), register.getRegisterDate());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
