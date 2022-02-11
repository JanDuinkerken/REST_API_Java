package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.appservice.AppServiceFactory;
import es.udc.ws.app.model.appservice.exceptions.*;
import es.udc.ws.app.model.register.Register;
import es.udc.ws.app.model.trip.Trip;

import es.udc.ws.app.thrift.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.TException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ThriftAppServiceImpl implements ThriftAppService.Iface{

    @Override
    public ThriftTripDto addTrip(ThriftTripDto tripDto) throws ThriftInputValidationException, ThriftLateTripException, TException {
        Trip trip = TripToThriftTripDtoConversor.toTrip(tripDto);

        try {
            Trip addedTrip = AppServiceFactory.getService().addTrip(trip);
            return TripToThriftTripDtoConversor.toThriftTripDto(addedTrip);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }catch (LateTripException e) {
            throw new ThriftLateTripException(e.getStartDate().toString());
        }
    }


    @Override
    public void updateTrip(ThriftTripDto tripDto) throws ThriftInputValidationException, ThriftLateUpdateTripException, ThriftParticipantsUpdateException, TException {
        Trip trip = TripToThriftTripDtoConversor.toTrip(tripDto);

        try {
            trip.setNumParticipants(-1);
            AppServiceFactory.getService().updateTrip(trip);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        }catch (LateUpdateTripException e) {
            throw new ThriftLateUpdateTripException(e.getTripId(), e.getStartDate().toString());
        }catch (ParticipantsUpdateException e) {
            throw new ThriftParticipantsUpdateException(e.getMaxParticipants(), e.getNumParticipants());
        }
    }

    @Override
    public List<ThriftTripDto> findTrips(String city, String startDate, String finishDate) throws ThriftInvalidDatesException {

        try {
            List<Trip> trips = AppServiceFactory.getService().findTrips(city, LocalDate.parse(startDate), LocalDate.parse(finishDate));
            return TripToThriftTripDtoConversor.toThriftTripDtos(trips);

        } catch (InvalidDatesException e) {
            throw new ThriftInvalidDatesException(e.getStartDate().toString(), e.getFinishDate().toString());
        }
    }

    @Override
    public long addRegister(long tripId, String email, int numReserves, String creditCard) throws TException {

        try {
            Register auxRegister = new Register(tripId, email, numReserves, creditCard);
            Register register = AppServiceFactory.getService().addRegister(auxRegister);
            return RegisterToThriftRegisterDtioConversor.toThriftRegisterDto(register).getRegisterId();
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        } catch (LateRegisterException e) {
            throw new ThriftLateRegisterException(e.getRegisterId());
        } catch (MaxParticipantsException e) {
            throw new ThriftMaxParticipantsException(e.getRegisterId(), e.getNumReserves(), e.getFreePlaces());
        } catch (AlreadyRegisteredException e) {
            throw new ThriftAlreadyRegisteredException(e.getRegisterId());
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        }
    }

    @Override
    public void cancelRegister(long registerId, String email) throws ThriftInputValidationException, TException {

        try {
            AppServiceFactory.getService().cancelRegister(registerId, email);
        } catch (LateCancelException e) {
            throw new ThriftLateCancelException(e.getRegisterId());
        } catch (AlreadyCancelledException e) {
            throw new ThriftAlreadyCancelledException(e.getRegisterId(), e.getCancelDate().toString());
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        } catch (DifferentUserException e) {
            throw new ThriftDifferentUserException(e.getRegisterId(), e.getEmail());
        }
    }

    @Override
    public List<ThriftRegisterDto> findRegister(String email) throws TException {

        try {
            List<Register> register = AppServiceFactory.getService().findRegisters(email);
            return RegisterToThriftRegisterDtioConversor.toThriftRegisterDto(register);

        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

}
