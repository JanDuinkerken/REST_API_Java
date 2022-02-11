package es.udc.ws.app.model.appservice;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import es.udc.ws.app.model.appservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.model.trip.SqlTripDao;
import es.udc.ws.app.model.trip.SqlTripDaoFactory;
import es.udc.ws.app.model.register.Register;
import es.udc.ws.app.model.register.SqlRegisterDao;
import es.udc.ws.app.model.register.SqlRegisterDaoFactory;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;
import static es.udc.ws.app.model.util.ModelConstants.*;

public class AppServiceImpl implements AppService {

    private final DataSource dataSource;
    private SqlTripDao tripDao = null;
    private SqlRegisterDao registerDao = null;

    public AppServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        tripDao = SqlTripDaoFactory.getDao();
        registerDao = SqlRegisterDaoFactory.getDao();
    }

    private void validateTrip(Trip trip) throws InputValidationException {

        PropertyValidator.validateMandatoryString("city", trip.getCity());
        PropertyValidator.validateMandatoryString("description", trip.getDescription());
        PropertyValidator.validateDouble("price", trip.getPrice(), 0, 9999);
        PropertyValidator.validateDouble("maxParticipants", trip.getMaxParticipants(), 1, 9999);
        PropertyValidator.validateDouble("numParticipants", trip.getNumParticipants(), -1, 9999);
    }

    private void validateRegister(Trip trip, Register register) throws InputValidationException, LateRegisterException, MaxParticipantsException {

        PropertyValidator.validateMandatoryString("email", register.getEmail());
        PropertyValidator.validateDouble("numReserves", register.getNumReserves(), RESERVE_MIN_PARTICIPANTS, RESERVE_MAX_PARTICIPANTS);
        PropertyValidator.validateCreditCard(register.getCreditCard());
        PropertyValidator.validateDouble("price", register.getPrice(), 0, 9999);
        if (ChronoUnit.HOURS.between(LocalDateTime.now(), trip.getStartDate()) < ADD_RESERVE_MIN_HOURS_START)
            throw new LateRegisterException(register.getRegisterId());
        if (trip.getMaxParticipants() - (register.getNumReserves() + trip.getNumParticipants()) < 0)
            throw new MaxParticipantsException(register.getRegisterId(), register.getNumReserves(), trip.getFreePlaces());
        if (register.getEmail().split("@").length != 2)
            throw new InputValidationException("Invalid email format");
        if (register.getCreditCard().length() != VALID_CREDIT_CARD.length())
            throw new InputValidationException("Invalid credit card format");
    }

    @Override
    public Trip addTrip(Trip trip) throws InputValidationException, LateTripException {
        validateTrip(trip);
        if (trip.getStartDate() == null) {
            throw new InputValidationException("Start date cannot be null");
        }
        if (ChronoUnit.HOURS.between(LocalDateTime.now(), trip.getStartDate()) < ADD_TRIP_MIN_HOURS_START) {
            throw new LateTripException(trip.getStartDate());
        }
        trip.setCreationDate(LocalDateTime.now());
        trip.setNumParticipants(0);
        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Trip createdTrip = tripDao.addTrip(connection, trip);

                /* Commit. */
                connection.commit();

                return createdTrip;

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

    @Override
    public void updateTrip(Trip trip) throws InstanceNotFoundException, InputValidationException, LateUpdateTripException, ParticipantsUpdateException {
        validateTrip(trip);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Trip initTrip = tripDao.findTrip(connection, trip.getTripId());
                if (ChronoUnit.HOURS.between(LocalDateTime.now(), trip.getStartDate()) < UPDATE_TRIP_MIN_HOURS_START
                        || initTrip.getStartDate().isBefore(LocalDateTime.now())) {
                    throw new LateUpdateTripException(trip.getTripId(), trip.getStartDate());
                }
                if (ChronoUnit.HOURS.between(initTrip.getStartDate(), trip.getStartDate()) < 0) {
                    throw new InputValidationException("Cannot change start date to a sooner date");
                }
                if (trip.getMaxParticipants() < initTrip.getNumParticipants()) {
                    throw new ParticipantsUpdateException(trip.getMaxParticipants(), initTrip.getNumParticipants());
                }
                if (trip.getMaxParticipants() < trip.getNumParticipants()) {
                    throw new ParticipantsUpdateException(trip.getMaxParticipants(), trip.getNumParticipants());
                }
                if (trip.getNumParticipants() == -1)
                    trip.setNumParticipants(initTrip.getNumParticipants());
                tripDao.updateTrip(connection, trip);

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

    @Override
    public List<Trip> findTrips(String city, LocalDate startDate, LocalDate finishDate) throws InvalidDatesException {

        if (startDate != null && finishDate != null) {
            if (ChronoUnit.DAYS.between(startDate, finishDate) < 0)
                throw new InvalidDatesException(startDate, finishDate);
        }
        try (Connection connection = dataSource.getConnection()) {
            return tripDao.findTrips(connection, city, startDate, finishDate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Register addRegister(Register register) throws InputValidationException, LateRegisterException, MaxParticipantsException, AlreadyRegisteredException, InstanceNotFoundException {

        try (Connection connection = dataSource.getConnection()) {
            try {
                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Trip trip = tripDao.findTrip(connection, register.getTripId());
                validateRegister(trip, register);
                register.setPrice(trip.getPrice());
                List<Register> checkRegister = findRegisters(register.getEmail());
                for (Register value : checkRegister) {
                    if (trip.getTripId() == value.getTripId()) {
                        throw new AlreadyRegisteredException(value.getRegisterId());
                    }
                }
                register.setRegisterDate(LocalDateTime.now());
                Register createdRegister = registerDao.addRegister(connection, register);
                trip.setNumParticipants(trip.getNumParticipants() + register.getNumReserves());
                tripDao.updateTrip(connection, trip);

                /* Commit. */
                connection.commit();
                return createdRegister;

            } catch (InstanceNotFoundException | LateRegisterException | MaxParticipantsException | AlreadyRegisteredException e) {
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

    @Override
    public List<Register> findRegisters(String email) throws InputValidationException {
        if (email.split("@").length != 2)
            throw new InputValidationException("Invalid email format");

        try (Connection connection = dataSource.getConnection()) {
            return registerDao.findAllUserRegisters(connection, email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelRegister(Long registerId, String email) throws AlreadyCancelledException, LateCancelException, DifferentUserException, InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            try {
                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Register register = registerDao.findRegister(connection, registerId);

                if (!register.getEmail().equals(email))
                    throw new DifferentUserException(registerId, email);

                if (register.getCancelDate() != null)
                    throw new AlreadyCancelledException(registerId, register.getCancelDate());

                if (LocalDateTime.now().withNano(0).plusHours(48).isAfter(tripDao.findTrip(connection, register.getTripId()).getStartDate()))
                    throw new LateCancelException(registerId);

                Trip trip = tripDao.findTrip(connection, register.getTripId());
                trip.setNumParticipants(trip.getNumParticipants() - register.getNumReserves());

                Timestamp registerDate = Timestamp.valueOf(register.getRegisterDate());
                Timestamp cancelDate = Timestamp.valueOf(LocalDateTime.now().withNano(0));

                // Cancel Register
                registerDao.updateRegister(connection, register, register.getEmail(), register.getNumReserves(),
                        register.getCreditCard(), register.getPrice(), registerDate, cancelDate);

                tripDao.updateTrip(connection, trip);

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
}