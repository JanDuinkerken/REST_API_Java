package es.udc.ws.app.client.service.exceptions;

import java.time.LocalDateTime;

import static es.udc.ws.app.client.service.util.ModelConstants.ADD_TRIP_MIN_HOURS_START;

public class ClientLateTripException extends Exception {

    private LocalDateTime startDate;

    public ClientLateTripException(LocalDateTime startDate) {
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
