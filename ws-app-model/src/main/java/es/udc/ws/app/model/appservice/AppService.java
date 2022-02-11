package es.udc.ws.app.model.appservice;

import java.time.LocalDate;
import java.util.List;

import es.udc.ws.app.model.appservice.exceptions.*;
import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.model.register.Register;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface AppService {

    Trip addTrip(Trip trip) throws InputValidationException, LateTripException;

    void updateTrip(Trip trip)
            throws InstanceNotFoundException, InputValidationException, LateUpdateTripException, ParticipantsUpdateException;

    List<Trip> findTrips(String city, LocalDate startDate, LocalDate finishDate)
            throws InvalidDatesException;

    Register addRegister(Register register) throws InputValidationException, LateRegisterException, MaxParticipantsException, AlreadyRegisteredException, InstanceNotFoundException;

    List<Register> findRegisters(String email) throws InputValidationException;

    void cancelRegister(Long registerId, String email) throws AlreadyCancelledException, LateCancelException, DifferentUserException, InstanceNotFoundException;

}
