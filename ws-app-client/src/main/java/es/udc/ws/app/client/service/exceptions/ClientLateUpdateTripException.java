package es.udc.ws.app.client.service.exceptions;

import java.time.LocalDateTime;

import static es.udc.ws.app.client.service.util.ModelConstants.UPDATE_TRIP_MIN_HOURS_START;

public class ClientLateUpdateTripException extends Exception{
    private Long tripId;
    private LocalDateTime startDate;

    public ClientLateUpdateTripException(Long tripId, LocalDateTime startDate) {
        super("Trip with id = " + tripId + " cannot be updated because the trip starts in less than " +
                UPDATE_TRIP_MIN_HOURS_START + " hours.");
        this.tripId = tripId;
        this.startDate = startDate;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
}
