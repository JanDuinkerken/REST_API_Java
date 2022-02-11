package es.udc.ws.app.model.trip;

import java.sql.*;

public class Jdbc3CcSqlTripDao extends AbstractSqlTripDao {

    @Override
    public Trip addTrip(Connection connection, Trip trip) {
        /* Create "queryString". */
        String queryString = "INSERT INTO Trip"
                + " (city, description, maxParticipants, freePlaces, price, startDate, creationDate)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, trip.getCity());
            preparedStatement.setString(i++, trip.getDescription());
            preparedStatement.setInt(i++, trip.getMaxParticipants());
            preparedStatement.setInt(i++, trip.getFreePlaces());
            preparedStatement.setFloat(i++, trip.getPrice());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(trip.getStartDate()));
            preparedStatement.setTimestamp(i, Timestamp.valueOf(trip.getCreationDate()));

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            long tripId = resultSet.getLong(1);

            /* Return Trip. */
            return new Trip(tripId, trip.getCity(), trip.getDescription(),
                    trip.getStartDate(), trip.getPrice(), trip.getMaxParticipants(),
                    trip.getCreationDate());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
