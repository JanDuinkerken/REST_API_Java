package es.udc.ws.app.restservice.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.ws.app.model.appservice.exceptions.*;
import es.udc.ws.app.restservice.dto.RestTripDto;
import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.model.appservice.AppServiceFactory;
import es.udc.ws.app.restservice.json.JsonToRestTripDtoConversor;
import es.udc.ws.app.restservice.dto.TripToRestTripDtoConversor;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;

public class TripServlet extends RestHttpServletTemplate {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException {

        try {
            ServletUtils.checkEmptyPath(req);
            RestTripDto tripDto = JsonToRestTripDtoConversor.toRestTripDto(req.getInputStream());
            Trip trip = TripToRestTripDtoConversor.toTrip(tripDto);

            trip = AppServiceFactory.getService().addTrip(trip);

            tripDto = TripToRestTripDtoConversor.toRestTripDto(trip);
            String tripURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + trip.getTripId();
            Map<String, String> headers = new HashMap<>(1);
            headers.put("Location", tripURL);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                    JsonToRestTripDtoConversor.toObjectNode(tripDto), headers);
        } catch (LateTripException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                    AppExceptionToJsonConversor.toLateTripException(ex), null);
        }
    }

    @Override
    protected void processPut(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {

        try {
            Long tripId = ServletUtils.getIdFromPath(req, "trip");
            RestTripDto tripDto = JsonToRestTripDtoConversor.toRestTripDto(req.getInputStream());
            if (!tripId.equals(tripDto.getTripId())) {
                throw new InputValidationException("Invalid Request: invalid tripId");
            }
            Trip trip = TripToRestTripDtoConversor.toTrip(tripDto);
            trip.setNumParticipants(-1);
            AppServiceFactory.getService().updateTrip(trip);

        } catch (LateUpdateTripException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                    AppExceptionToJsonConversor.toLateUpdateTripException(ex), null);
            return;
        } catch (ParticipantsUpdateException ex) {
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                AppExceptionToJsonConversor.toParticipantsUpdateException(ex), null);
        return;
        }

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException {
        ServletUtils.checkEmptyPath(req);
        String city = ServletUtils.getMandatoryParameter(req,"city");
        LocalDate startDate = LocalDate.parse(ServletUtils.getMandatoryParameter(req,"startDate"));
        LocalDate finishDate = LocalDate.parse(ServletUtils.getMandatoryParameter(req,"finishDate"));

        List<Trip> trips = null;
        try {
            trips = AppServiceFactory.getService().findTrips(city, startDate, finishDate);
        } catch (InvalidDatesException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                    AppExceptionToJsonConversor.toInvalidDatesException(ex), null);
            return;
        }

        List<RestTripDto> tripDtos = TripToRestTripDtoConversor.toRestTripDtos(trips);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                JsonToRestTripDtoConversor.toArrayNode(tripDtos), null);
    }
}
