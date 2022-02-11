package es.udc.ws.app.model.appservice.exceptions;

import java.time.LocalDateTime;

import static es.udc.ws.app.model.util.ModelConstants.UPDATE_TRIP_MIN_HOURS_START;

public class LateUpdateTripException extends Exception{
    private Long tripId;
    private LocalDateTime startDate;

    public LateUpdateTripException(Long tripId, LocalDateTime startDate) {
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
