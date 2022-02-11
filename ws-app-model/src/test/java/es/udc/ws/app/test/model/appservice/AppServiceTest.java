package es.udc.ws.app.test.model.appservice;

import static es.udc.ws.app.model.util.ModelConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import es.udc.ws.app.model.register.Register;
import es.udc.ws.app.model.register.SqlRegisterDao;
import es.udc.ws.app.model.register.SqlRegisterDaoFactory;
import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.model.trip.SqlTripDao;
import es.udc.ws.app.model.trip.SqlTripDaoFactory;
import es.udc.ws.app.model.appservice.AppService;
import es.udc.ws.app.model.appservice.AppServiceFactory;
import es.udc.ws.app.model.appservice.exceptions.*;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;


public class AppServiceTest {

    private final long NON_EXISTENT_TRIP_ID = -1;
    private final long NON_EXISTENT_REGISTER_ID = -1;

    private static AppService appService = null;

    private static SqlTripDao tripDao = null;
    private static SqlRegisterDao registerDao = null;

    @BeforeAll
    public static void init() {

        DataSource dataSource = new SimpleDataSource();

        DataSourceLocator.addDataSource(APP_DATA_SOURCE, dataSource);

        appService = AppServiceFactory.getService();

        tripDao = SqlTripDaoFactory.getDao();
        registerDao = SqlRegisterDaoFactory.getDao();
    }
    private Trip getValidTrip() {
        return getValidTrip("Santiago");
    }

    private Trip getValidTrip(String city) {
        return new Trip(city, "Viaje por " + city , LocalDateTime.now().withNano(0).plusHours(ADD_TRIP_MIN_HOURS_START+1), 10, 200);
    }

    private Trip getValidTrip(String city, LocalDateTime startDate) {
        return new Trip(city, "Viaje por " + city , startDate, 10, 200);
    }

    private Register getValidRegister(Trip trip) {
        return new Register(trip.getTripId(), "email@udc.es", 5, VALID_CREDIT_CARD, trip.getPrice());
    }

    private Trip createTrip(Trip trip) {

        Trip addedTrip = null;
        try {
            addedTrip = appService.addTrip(trip);
        } catch (InputValidationException | LateTripException e) {
            throw new RuntimeException(e);
        }
        return addedTrip;
    }

    private void removeTrip(Long tripId) throws InstanceNotFoundException{

        try (Connection connection = DataSourceLocator.getDataSource(APP_DATA_SOURCE).getConnection()) {
            try {
                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                tripDao.removeTrip(connection, tripId);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Trip findTrip(Long tripId) throws InstanceNotFoundException {
        try (Connection connection = DataSourceLocator.getDataSource(APP_DATA_SOURCE).getConnection()) {
            return tripDao.findTrip(connection, tripId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Register findRegister(Long registerId) throws InstanceNotFoundException {
        try (Connection connection = DataSourceLocator.getDataSource(APP_DATA_SOURCE).getConnection()) {
            return registerDao.findRegister(connection, registerId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void cancelRegister(Long registerId, String email) {

        try {
            appService.cancelRegister(registerId, email);
        } catch (AlreadyCancelledException | LateCancelException | DifferentUserException | InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeRegister(Long registerId) throws InstanceNotFoundException {

        try (Connection connection = DataSourceLocator.getDataSource(APP_DATA_SOURCE).getConnection()) {
            try {
                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                registerDao.removeRegister(connection, registerId);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAddTripAndFindTrip() throws InputValidationException, InstanceNotFoundException {

        Trip trip = getValidTrip();
        Trip addedTrip = null;

        try {

            // Create Trip
            LocalDateTime beforeCreationDate = LocalDateTime.now().withNano(0);

            addedTrip = appService.addTrip(trip);

            LocalDateTime afterCreationDate = LocalDateTime.now().withNano(0);

            // Find Register
            Trip foundTrip = findTrip(addedTrip.getTripId());

            assertEquals(addedTrip, foundTrip);
            assertEquals(foundTrip.getCity(),trip.getCity());
            assertEquals(foundTrip.getDescription(),trip.getDescription());
            assertEquals(foundTrip.getStartDate(),trip.getStartDate());
            assertEquals(foundTrip.getPrice(),trip.getPrice());
            assertEquals(foundTrip.getMaxParticipants(),trip.getMaxParticipants());
            assertEquals(foundTrip.getNumParticipants(), trip.getNumParticipants());

            assertTrue((foundTrip.getCreationDate().compareTo(beforeCreationDate) >= 0)
                    && (foundTrip.getCreationDate().compareTo(afterCreationDate) <= 0));

        } catch (LateTripException e) {
            e.printStackTrace();
        } finally {
            // Clear Database
            if (addedTrip!=null) {
                removeTrip(addedTrip.getTripId());
            }
        }
    }

    @Test
    public void testAddInvalidTrip() {
        assertThrows(InputValidationException.class, () -> {
            Trip trip = getValidTrip();
            trip.setCity(null);
            Trip addedTrip = appService.addTrip(trip);
            removeTrip(addedTrip.getTripId());
        });
        assertThrows(InputValidationException.class, () -> {
            Trip trip = getValidTrip();
            trip.setDescription(null);
            Trip addedTrip = appService.addTrip(trip);
            removeTrip(addedTrip.getTripId());
        });
        assertThrows(NullPointerException.class, () -> {
            Trip trip = getValidTrip();
            trip.setMaxParticipants(null);
            Trip addedTrip = appService.addTrip(trip);
            removeTrip(addedTrip.getTripId());
        });
        assertThrows(InputValidationException.class, () -> {
            Trip trip = getValidTrip();
            trip.setMaxParticipants(-100);
            Trip addedTrip = appService.addTrip(trip);
            removeTrip(addedTrip.getTripId());
        });
        assertThrows(NullPointerException.class, () -> {
            Trip trip = getValidTrip();
            trip.setNumParticipants(null);
            Trip addedTrip = appService.addTrip(trip);
            removeTrip(addedTrip.getTripId());
        });
        assertThrows(InputValidationException.class, () -> {
            Trip trip = getValidTrip();
            trip.setNumParticipants(-100);
            Trip addedTrip = appService.addTrip(trip);
            removeTrip(addedTrip.getTripId());
        });
        assertThrows(InputValidationException.class, () -> {
            Trip trip = getValidTrip();
            trip.setStartDate(null);
            Trip addedTrip = appService.addTrip(trip);
            removeTrip(addedTrip.getTripId());
        });
        assertThrows(NullPointerException.class, () -> {
            Trip trip = getValidTrip();
            trip.setPrice(null);
            Trip addedTrip = appService.addTrip(trip);
            removeTrip(addedTrip.getTripId());
        });
        assertThrows(LateTripException.class, () -> {
            Trip trip = getValidTrip();
            trip.setStartDate(trip.getStartDate().minusHours(3));
            Trip addedTrip = appService.addTrip(trip);
            removeTrip(addedTrip.getTripId());
        });
    }

    @Test
    public void testUpdateTrip() throws InputValidationException, InstanceNotFoundException, LateUpdateTripException, ParticipantsUpdateException {

        Trip trip = createTrip(getValidTrip());
        try {
            trip.setMaxParticipants(trip.getMaxParticipants() + 1);
            trip.setStartDate(trip.getStartDate().plusHours(1));

            appService.updateTrip(trip);

            Trip updatedTrip = findTrip(trip.getTripId());

            trip.setCreationDate(trip.getCreationDate());
            assertEquals(trip, updatedTrip);

        } finally {
            // Clear Database
            removeTrip(trip.getTripId());
        }

    }

    @Test
    public void testUpdateInvalidTrip() throws InstanceNotFoundException, ParticipantsUpdateException {

        Long tripId = createTrip(getValidTrip()).getTripId();
        try {
            // Check city not null
            Trip trip = findTrip(tripId);
            trip.setCity(null);
            assertThrows(InputValidationException.class, () -> appService.updateTrip(trip));
        } finally {
            // Clear Database
            removeTrip(tripId);
        }

        tripId = createTrip(getValidTrip()).getTripId();
        try {
            // Check dates
            Trip trip = findTrip(tripId);
            trip.setStartDate(trip.getStartDate().minusHours(1));
            assertThrows(LateUpdateTripException.class, () -> appService.updateTrip(trip));
        } finally {
            // Clear Database
            removeTrip(tripId);
        }
        tripId = createTrip(getValidTrip()).getTripId();
        try {
            // Check participants
            Trip trip = findTrip(tripId);
            trip.setNumParticipants(100);
            trip.setMaxParticipants(110);
            appService.updateTrip(trip);
            trip.setMaxParticipants(90);
            assertThrows(ParticipantsUpdateException.class, () -> appService.updateTrip(trip));
        } catch (LateUpdateTripException | InputValidationException e) {
            e.printStackTrace();
        } finally {
            // Clear Database
            removeTrip(tripId);
        }
        tripId = createTrip(getValidTrip()).getTripId();
        try {
            // Check participants
            Trip trip = findTrip(tripId);
            trip.setNumParticipants(100);
            trip.setMaxParticipants(90);
            assertThrows(ParticipantsUpdateException.class, () -> appService.updateTrip(trip));
        } finally {
            // Clear Database
            removeTrip(tripId);
        }
    }

    @Test
    public void testUpdateNonExistentTrip() {

        Trip trip = getValidTrip();
        trip.setTripId(NON_EXISTENT_TRIP_ID);
        trip.setCreationDate(LocalDateTime.now());

        assertThrows(InstanceNotFoundException.class, () -> appService.updateTrip(trip));
    }

    @Test
    public void testFindTripsByCity() throws InstanceNotFoundException {

        // Add Trips
        List<Trip> trips = new LinkedList<>();
        Trip trip1 = createTrip(getValidTrip("A Coruña"));
        Trip trip2 = createTrip(getValidTrip("A Coruña"));
        trips.add(trip1);
        trips.add(trip2);

        try {
            List<Trip> foundTrips = appService.findTrips("A Coruña", null, null);
            assertEquals(trips, foundTrips);
            assertEquals(2, foundTrips.size());
            assertEquals(trips.get(0), foundTrips.get(0));

            foundTrips = appService.findTrips("FIC", null, null);
            assertEquals(0, foundTrips.size());

        } catch (InvalidDatesException e) {
            e.printStackTrace();
        } finally {
            for (Trip trip : trips) {
                removeTrip(trip.getTripId());
            }
        }
    }

    @Test
    public void testFindTripsByCityAndDates() throws InstanceNotFoundException {

        // Add Trips
        List<Trip> trips = new LinkedList<>();
        Trip trip1 = createTrip(getValidTrip("Lugo", LocalDateTime.now().plusDays(15).withNano(0)));
        Trip trip2 = createTrip(getValidTrip("Lugo", LocalDateTime.now().plusDays(4).withNano(0)));
        trips.add(trip1);
        trips.add(trip2);

        try {
            List<Trip> foundTrips = appService.findTrips("Lugo", LocalDate.now().plusDays(10), LocalDate.now().plusDays(18));
            assertNotEquals(trips, foundTrips);
            assertEquals(1, foundTrips.size());
            assertEquals(trips.get(0), foundTrips.get(0));

            foundTrips = appService.findTrips("FIC", null, null);
            assertEquals(0, foundTrips.size());

        } catch (InvalidDatesException e) {
            e.printStackTrace();
        } finally {
            for (Trip trip : trips) {
                removeTrip(trip.getTripId());
            }
        }
    }

    @Test
    public void testAddRegisterAndFindRegister() throws InstanceNotFoundException {

        Trip trip = getValidTrip();
        Trip addedTrip = null;
        Register register;
        Register addedRegister= null;

        try {
            // Create Trip
            addedTrip = appService.addTrip(trip);

            // Create Register
            register = getValidRegister(addedTrip);

            LocalDateTime beforeCreationDate = LocalDateTime.now().withNano(0);

            addedRegister = appService.addRegister(register);

            LocalDateTime afterCreationDate = LocalDateTime.now().withNano(0);

            // Find Register
            Register foundRegister = findRegister(addedRegister.getRegisterId());

            register.setRegisterId(addedRegister.getRegisterId());
            assertEquals(register, foundRegister);
            assertEquals(foundRegister.getCancelDate(), register.getCancelDate());
            assertEquals(foundRegister.getCreditCard(), register.getCreditCard());
            assertEquals(foundRegister.getEmail(), register.getEmail());
            assertEquals(foundRegister.getNumReserves(), register.getNumReserves());
            assertTrue((foundRegister.getRegisterDate().compareTo(beforeCreationDate) >= 0)
                    && (foundRegister.getRegisterDate().compareTo(afterCreationDate) <= 0));

        } catch (InstanceNotFoundException | InputValidationException | MaxParticipantsException | LateRegisterException | LateTripException | AlreadyRegisteredException e) {
            e.printStackTrace();
        } finally {
            // Clear Database
            if (addedRegister!=null) {
                removeRegister(addedRegister.getRegisterId());
            }
            if(addedTrip!=null){
                removeTrip(addedTrip.getTripId());
            }
        }
    }

    @Test
    public void testAddInvalidRegister() throws InstanceNotFoundException {

        Trip trip = getValidTrip();
        Trip addTrip = null;

        try {
            // Create Trip
            addTrip = appService.addTrip(trip);
            Trip addedTrip = addTrip;

            // Check register email not null
            assertThrows(InputValidationException.class, () -> {
                Register register = getValidRegister(addedTrip);
                register.setEmail(null);
                Register addedRegister = appService.addRegister(register);
                removeRegister(addedRegister.getRegisterId());
            });

            // Check register email not empty
            assertThrows(InputValidationException.class, () -> {
                Register register = getValidRegister(addedTrip);
                register.setEmail("");
                Register addedRegister = appService.addRegister(register);
                removeRegister(addedRegister.getRegisterId());
            });

            // Check register email correct pattern
            assertThrows(InputValidationException.class, () -> {
                Register register = getValidRegister(addedTrip);
                register.setEmail("email.udc.es");
                Register addedRegister = appService.addRegister(register);
                removeRegister(addedRegister.getRegisterId());
            });

            assertThrows(InputValidationException.class, () -> {
                Register register = getValidRegister(addedTrip);
                register.setEmail("email@udc@es");
                Register addedRegister = appService.addRegister(register);
                removeRegister(addedRegister.getRegisterId());
            });

            // Check numReserves <= 5, numReserves => 1
            assertThrows(InputValidationException.class, () -> {
                Register register = getValidRegister(addedTrip);
                register.setNumReserves(0);
                Register addedRegister = appService.addRegister(register);
                removeRegister(addedRegister.getRegisterId());
            });

            assertThrows(InputValidationException.class, () -> {
                Register register = getValidRegister(addedTrip);
                register.setNumReserves(6);
                Register addedRegister = appService.addRegister(register);
                removeRegister(addedRegister.getRegisterId());
            });

            // Check creditCard not null
            assertThrows(InputValidationException.class, () -> {
                Register register = getValidRegister(addedTrip);
                register.setCreditCard(null);
                Register addedRegister = appService.addRegister(register);
                removeRegister(addedRegister.getRegisterId());
            });

            // Check creditCard not empty
            assertThrows(InputValidationException.class, () -> {
                Register register = getValidRegister(addedTrip);
                register.setCreditCard("");
                Register addedRegister = appService.addRegister(register);
                removeRegister(addedRegister.getRegisterId());
            });

            // Check creditCard correct pattern
            assertThrows(InputValidationException.class, () -> {
                Register register = getValidRegister(addedTrip);
                register.setCreditCard("12345");
                Register addedRegister = appService.addRegister(register);
                removeRegister(addedRegister.getRegisterId());
            });

            // Check price >= 0
            assertThrows(InputValidationException.class, () -> {
                Register register = getValidRegister(addedTrip);
                register.setPrice(-5);
                Register addedRegister = appService.addRegister(register);
                removeRegister(addedRegister.getRegisterId());
            });
        } catch (InputValidationException | LateTripException e) {
            e.printStackTrace();
        } finally {
            // Clear Database
            if(addTrip!=null){
                removeTrip(addTrip.getTripId());
            }
        }
    }

    @Test
    public void testFindNonExistentRegister() {
        assertThrows(InstanceNotFoundException.class, () -> findRegister(NON_EXISTENT_REGISTER_ID));
    }

    @Test
    public void testAddRegisterToNonExistentTrip() {
        assertThrows(RuntimeException.class, () -> {
            Trip trip = getValidTrip();
            Register register = getValidRegister(trip);
            Register addedRegister = appService.addRegister(register);
            removeRegister(addedRegister.getRegisterId());
        });
    }

    @Test
    public void testRemoveRegister() throws InputValidationException, LateRegisterException, MaxParticipantsException, LateTripException, InstanceNotFoundException, AlreadyRegisteredException {

        Trip trip = getValidTrip();
        Trip addedTrip = appService.addTrip(trip);
        Register register = getValidRegister(addedTrip);
        Register addedRegister = appService.addRegister(register);

        removeRegister(addedRegister.getRegisterId());
        assertThrows(InstanceNotFoundException.class, () -> findRegister(addedRegister.getRegisterId()));

        removeTrip(addedTrip.getTripId());
    }

    @Test
    public void testRemoveNonExistentRegister() {
        assertThrows(InstanceNotFoundException.class, () -> removeRegister(NON_EXISTENT_REGISTER_ID));
    }

    @Test
    public void testCancelRegister() throws InputValidationException, LateTripException, LateRegisterException, MaxParticipantsException, InstanceNotFoundException, AlreadyRegisteredException {

        Trip trip = getValidTrip();
        Trip addedTrip = appService.addTrip(trip);
        Register register = getValidRegister(addedTrip);
        Register addedRegister = appService.addRegister(register);
        int numFreePlaces1 = findTrip(register.getTripId()).getFreePlaces();

        assertThrows(DifferentUserException.class, () -> appService.cancelRegister(addedRegister.getRegisterId(), "wrong@email.com"));

        assertNull(findRegister(addedRegister.getRegisterId()).getCancelDate());

        cancelRegister(addedRegister.getRegisterId(), register.getEmail());
        assertThrows(AlreadyCancelledException.class, () -> appService.cancelRegister(addedRegister.getRegisterId(), register.getEmail()));

        assertNotNull(findRegister(addedRegister.getRegisterId()).getCancelDate());

        int numFreePlaces2 = findTrip(register.getTripId()).getFreePlaces();

        assertTrue(numFreePlaces1 < numFreePlaces2);

        removeRegister(addedRegister.getRegisterId());
        removeTrip(addedTrip.getTripId());
    }

    @Test
    public void testFindAllUserRegisters() throws InputValidationException, LateRegisterException, MaxParticipantsException, LateTripException, InstanceNotFoundException, AlreadyRegisteredException {

        Trip trip = getValidTrip("Coruña");
        Trip trip2 = getValidTrip("Ourense");
        Trip addedTrip = appService.addTrip(trip);
        Trip addedTrip2 = appService.addTrip(trip2);
        Register register = getValidRegister(addedTrip);
        Register register2 = getValidRegister(addedTrip2);
        Register addedRegister = appService.addRegister(register);
        Register addedRegister2 = appService.addRegister(register2);

        List<Register> L1 = appService.findRegisters("email@udc.es");
        assertEquals(2, L1.size());

        List<Register> L2 = appService.findRegisters("malemail@udc.es");
        assertEquals(0, L2.size());

        removeRegister(addedRegister.getRegisterId());
        L1 = appService.findRegisters("email@udc.es");
        assertEquals(1, L1.size());

        removeRegister(addedRegister2.getRegisterId());
        L1 = appService.findRegisters("email@udc.es");
        assertEquals(0, L1.size());

        removeTrip(addedTrip.getTripId());
        removeTrip(addedTrip2.getTripId());
    }
}