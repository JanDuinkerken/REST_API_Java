package es.udc.ws.app.client.service.rest;

import es.udc.ws.app.client.service.dto.ClientRegisterDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientRegisterDtoConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientTripDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import es.udc.ws.app.client.service.ClientAppService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RestClientAppService implements ClientAppService{

    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientAppService.endpointAddress";
    private String endpointAddress;

    public long addTrip(ClientTripDto trip) throws InputValidationException {

        try {
            HttpResponse response = Request.Post(getEndpointAddress() + "trips").
                    bodyStream(toInputStreamTrip(trip), ContentType.create("application/json")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientTripDtoConversor.toClientTripDto(response.getEntity().getContent()).getTripId();

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTrip(ClientTripDto trip) throws InputValidationException, InstanceNotFoundException {

            try {
                HttpResponse response = Request.Put(getEndpointAddress() + "trips/" + trip.getTripId()).
                        bodyStream(toInputStreamTrip(trip), ContentType.create("application/json")).
                        execute().returnResponse();

                validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

            } catch (InputValidationException | InstanceNotFoundException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

    public List<ClientTripDto> findTrips(String city, LocalDate startDate, LocalDate finishDate) {
        try {
            HttpResponse response = Request.Get(getEndpointAddress() + "trips?" +
                            "city=" + URLEncoder.encode(city, "UTF-8") +
                            "&startDate=" + URLEncoder.encode(startDate.toString() , "UTF-8") +
                            "&finishDate=" + URLEncoder.encode(finishDate.toString() , "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientTripDtoConversor.toClientTripDtos(response.getEntity().getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long addRegister(Long tripId, String email, int numReserves, String creditCard) throws InputValidationException {

        try {
            HttpResponse response = Request.Post(getEndpointAddress() + "registers?" +
                            "tripId=" + URLEncoder.encode(tripId.toString(), "UTF-8") +
                            "&email=" + URLEncoder.encode(email , "UTF-8") +
                            "&numReserves=" + URLEncoder.encode(String.valueOf(numReserves), "UTF-8") +
                            "&creditCard=" + URLEncoder.encode(creditCard, "UTF-8")).
            execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientRegisterDtoConversor.toClientRegisterDto(response.getEntity().getContent()).getRegisterId();

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void cancelRegister(Long registerId, String email) throws InputValidationException, InstanceNotFoundException {

        try {
            HttpResponse response = Request.Put(getEndpointAddress() + "registers?" +
                            "registerId=" + URLEncoder.encode(registerId.toString(), "UTF-8") +
                            "&email=" + URLEncoder.encode(email, "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

        } catch (InputValidationException | InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<ClientRegisterDto> findRegister(String email) throws InputValidationException {
        try {
            HttpResponse response = Request.Get(getEndpointAddress() + "registers?" +
                            "email=" + URLEncoder.encode(email, "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientRegisterDtoConversor.toClientRegisterDtos(response.getEntity().getContent());

        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private InputStream toInputStreamTrip(ClientTripDto trip) {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientTripDtoConversor.toObjectNode(trip));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void validateStatusCode(int successCode, HttpResponse response) throws Exception {

        try {
            int statusCode = response.getStatusLine().getStatusCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {

                case HttpStatus.SC_NOT_FOUND:
                    throw JsonToClientExceptionConversor.fromNotFoundErrorCode(response.getEntity().getContent());

                case HttpStatus.SC_BAD_REQUEST:
                    throw JsonToClientExceptionConversor.fromBadRequestErrorCode(response.getEntity().getContent());

                case HttpStatus.SC_FORBIDDEN:
                    throw JsonToClientExceptionConversor.fromForbiddenErrorCode(response.getEntity().getContent());

                case HttpStatus.SC_GONE:
                    throw JsonToClientExceptionConversor.fromGoneErrorCode(response.getEntity().getContent());

                default:
                    throw new RuntimeException("HTTP error; status code = " + statusCode);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
