package es.udc.ws.app.model.register;

import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlRegisterDao implements SqlRegisterDao {

    protected AbstractSqlRegisterDao() {}

    @Override
    public Register findRegister(Connection connection, Long registerId) throws InstanceNotFoundException {

        String queryString = "SELECT tripId, email, numReserves, creditCard, price, registerDate, cancelDate"
                + " FROM Register WHERE registerId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i, registerId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(registerId, Register.class.getName());
            }

            i = 1;
            Long tripId = Long.valueOf(resultSet.getLong(i++));
            String email = resultSet.getString(i++);
            int numReserves = resultSet.getInt(i++);
            String creditCard = resultSet.getString(i++);
            float price = resultSet.getFloat(i++);
            Timestamp registerDateTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime registerDate = registerDateTimestamp.toLocalDateTime();
            Timestamp cancelDateTimestamp = resultSet.getTimestamp(i);
            LocalDateTime cancelDate = null;
            if (cancelDateTimestamp != null) {
                cancelDate = cancelDateTimestamp.toLocalDateTime();
            }

            return new Register(registerId, tripId, email, numReserves, creditCard, registerDate, price, cancelDate);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Register> findAllUserRegisters(Connection connection, String email) {

        String[] words = email != null ? email.split("@") : null;
        String queryString = "SELECT registerId, tripId, email, numReserves, creditCard, price, registerDate, cancelDate"
                + " FROM Register";
        if (words != null && words.length == 2) {
            queryString += " WHERE email = ?";
        }
        queryString += " ORDER BY registerDate";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            if (words != null) {
                preparedStatement.setString(1, email);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Register> registers = new ArrayList<>();

            while(resultSet.next()) {

                int i = 1;
                Long registerId = Long.valueOf(resultSet.getLong(i++));
                Long tripId = Long.valueOf(resultSet.getLong(i++));
                email = resultSet.getString(i++);
                int numReserves = resultSet.getInt(i++);
                String creditCard = resultSet.getString(i++);
                float price = resultSet.getFloat(i++);
                Timestamp registerDateTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime registerDate = registerDateTimestamp.toLocalDateTime();
                Timestamp cancelDateTimestamp = resultSet.getTimestamp(i);
                LocalDateTime cancelDate = cancelDateTimestamp != null ? cancelDateTimestamp.toLocalDateTime() : null;

                registers.add(new Register(registerId, tripId, email, numReserves, creditCard, registerDate, price, cancelDate));
            }

            return registers;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateRegister(Connection connection, Register register, String email, int numReserves,
                               String creditCard, Float price, Timestamp registerDate, Timestamp cancelDate) throws InstanceNotFoundException {

        String queryString =  "UPDATE Register SET email = ?, " +
                "numReserves = ?, creditCard = ?, price = ?, " +
                "registerDate = ?, cancelDate = ?  WHERE registerId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setString(i++, email);
            preparedStatement.setInt(i++, numReserves);
            preparedStatement.setString(i++, creditCard);
            preparedStatement.setFloat(i++, price);
            preparedStatement.setTimestamp(i++, registerDate);
            preparedStatement.setTimestamp(i++, cancelDate);
            preparedStatement.setLong(i, register.getRegisterId());

            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(register.getRegisterId(), Register.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeRegister(Connection connection, Long registerId) throws InstanceNotFoundException {
        /* Create "queryString". */
        String queryString = "DELETE FROM Register WHERE" + " registerId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i, registerId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(registerId, Register.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
