package es.udc.ws.app.model.trip;

import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.ADD_RESERVE_MIN_HOURS_START;


public abstract class AbstractSqlTripDao implements SqlTripDao {
    protected AbstractSqlTripDao(){
    }

    @Override
    public void updateTrip(Connection connection, Trip trip) throws InstanceNotFoundException {
        /* Create "queryString". */
        String queryString = "UPDATE Trip" +
                " SET  startDate= ?, maxParticipants = ?," +
                " price= ?, freePlaces = ? WHERE tripId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(trip.getStartDate()));
            preparedStatement.setInt(i++, trip.getMaxParticipants());
            preparedStatement.setFloat(i++, trip.getPrice());
            preparedStatement.setInt(i++, trip.getFreePlaces());
            preparedStatement.setLong(i, trip.getTripId());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new es.udc.ws.util.exceptions.InstanceNotFoundException(trip.getTripId(),
                        Trip.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Trip findTrip(Connection connection, Long tripId) throws InstanceNotFoundException {
        /* Create "queryString". */
        String queryString = "SELECT city, description, "
                + " price, maxParticipants, freePlaces, startDate, creationDate FROM Trip WHERE tripId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i, tripId);

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(tripId, Trip.class.getName());
            }

            /* Get results. */
            i = 1;
            String city = resultSet.getString(i++);
            String description = resultSet.getString(i++);
            float price = resultSet.getFloat(i++);
            int maxParticipants = resultSet.getInt(i++);
            int freePlaces = resultSet.getInt(i++);
            Timestamp startDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime startDate = startDateAsTimestamp.toLocalDateTime();
            Timestamp creationDateAsTimestamp = resultSet.getTimestamp(i);
            LocalDateTime creationDate = creationDateAsTimestamp.toLocalDateTime();

            /* Return trip. */
            return new Trip(tripId, city, description, startDate, price, maxParticipants, maxParticipants - freePlaces, creationDate);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Trip> findTrips(Connection connection, String city, LocalDate startDate, LocalDate finishDate) {
        /* Create "queryString". */
        String queryString = "SELECT tripId, city, description, "
                + " price, maxParticipants, freePlaces, startDate, creationDate FROM Trip" +
                " WHERE city=? && startDate >= " + Date.valueOf(LocalDate.now().plusDays(1));
        if (startDate != null && finishDate != null) {
            queryString += " && (startDate >= ? && startDate <= ?)";
        }

        queryString += " ORDER BY city";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setString(i++,city);
            if (startDate != null && finishDate!=null) {
                /* Fill "preparedStatement". */
                preparedStatement.setDate(i++, Date.valueOf(startDate));
                preparedStatement.setDate(i, Date.valueOf(finishDate));
            }

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Read trips. */
            List<Trip> trip = new ArrayList<>();

            while (resultSet.next()) {

                i = 1;
                Long tripId = Long.valueOf(resultSet.getLong(i++));
                String cityr = resultSet.getString(i++);
                String description = resultSet.getString(i++);
                float price = resultSet.getFloat(i++);
                int maxParticipants = resultSet.getInt(i++);
                int freePlaces = resultSet.getInt(i++);
                Timestamp startDateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime startDater = startDateAsTimestamp.toLocalDateTime();
                Timestamp creationDateAsTimestamp = resultSet.getTimestamp(i);
                LocalDateTime creationDate = creationDateAsTimestamp.toLocalDateTime();

                if (ChronoUnit.HOURS.between(LocalDateTime.now(), startDater) > ADD_RESERVE_MIN_HOURS_START) {
                    trip.add(new Trip(tripId, cityr, description, startDater, price, maxParticipants, maxParticipants - freePlaces, creationDate));
                }
            }

            /* Return trips. */
            return trip;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeTrip(Connection connection, Long tripId) throws InstanceNotFoundException {
        /* Create "queryString". */
        String queryString = "DELETE FROM Trip WHERE" + " tripId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i, tripId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(tripId,
                        Trip.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
