package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.ClientAppService;
import es.udc.ws.app.client.service.dto.ClientRegisterDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.time.LocalDate;
import java.util.List;

public class ThriftClientAppService implements ClientAppService {
    private final static String ENDPOINT_ADDRESS_PARAMETER =
            "ThriftClientAppService.endpointAddress";

    private final static String endpointAddress =
            ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);

    @Override
    public long addTrip(ClientTripDto trip) throws InputValidationException {
        ThriftAppService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try  {

            transport.open();

            return client.addTrip(ClientTripDtoToThriftTripDtoConversor.toThriftTripDto(trip)).getTripId();

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }
    }

    @Override
    public void updateTrip(ClientTripDto trip) throws InputValidationException, InstanceNotFoundException {
        ThriftAppService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try  {

            transport.open();
            client.updateTrip(ClientTripDtoToThriftTripDtoConversor.toThriftTripDto(trip));

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }
    }

    @Override
    public List<ClientTripDto> findTrips(String city, LocalDate startDate, LocalDate finishDate) {

        ThriftAppService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try {

            transport.open();
            return ClientTripDtoToThriftTripDtoConversor.toClientTripDto(client.findTrips(city, startDate.toString(), finishDate.toString()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }
    }

    @Override
    public long addRegister(Long tripId, String email, int numReserves, String creditCard) throws InputValidationException {
        ThriftAppService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try  {

            transport.open();
            return client.addRegister(tripId, email, numReserves, creditCard);

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }
    }

    @Override
    public void cancelRegister(Long registerId, String email) throws InputValidationException, InstanceNotFoundException {
        ThriftAppService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try  {

            transport.open();
            client.cancelRegister(registerId, email);

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }
    }

    @Override
    public List<ClientRegisterDto> findRegister(String email) throws InputValidationException {

        ThriftAppService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try {

            transport.open();
            return ClientRegisterDtoToThriftRegisterDtoConversor.toClientRegisterDtos(client.findRegister(email));

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }
    }

    private ThriftAppService.Client getClient() {

        try {

            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftAppService.Client(protocol);

        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }

    }
}

