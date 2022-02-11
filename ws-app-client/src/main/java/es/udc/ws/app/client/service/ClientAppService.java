package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientRegisterDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface ClientAppService {

    public long addTrip(ClientTripDto trip) throws InputValidationException;

    public void updateTrip(ClientTripDto trip) throws InputValidationException, InstanceNotFoundException;

    public List<ClientTripDto> findTrips(String city, LocalDate startDate, LocalDate finishDate);

    public long addRegister(Long tripId, String email, int numReserves, String creditCard) throws InputValidationException;

    public void cancelRegister(Long registerId, String email) throws InputValidationException, InstanceNotFoundException;

    public List<ClientRegisterDto> findRegister(String email) throws InputValidationException;
}
