package es.udc.ws.app.model.trip;

import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public interface SqlTripDao {

    Trip addTrip(Connection connection, Trip trip);

    void updateTrip(Connection connection, Trip trip) throws InstanceNotFoundException;

    Trip findTrip(Connection connection, Long tripId) throws InstanceNotFoundException;

    List<Trip> findTrips(Connection connection, String city, LocalDate startDate, LocalDate finishDate);

    void removeTrip(Connection connection, Long tripId) throws InstanceNotFoundException;
}
