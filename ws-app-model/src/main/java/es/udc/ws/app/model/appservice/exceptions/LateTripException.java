package es.udc.ws.app.model.appservice.exceptions;

import java.time.LocalDateTime;

import static es.udc.ws.app.model.util.ModelConstants.ADD_TRIP_MIN_HOURS_START;

public class LateTripException extends Exception {

    private LocalDateTime startDate;

    public LateTripException(LocalDateTime startDate) {
        super("Trip cannot be created because it starts in less than " +
                ADD_TRIP_MIN_HOURS_START + " hours.");
        this.startDate = startDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
}
