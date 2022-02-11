package es.udc.ws.app.model.appservice.exceptions;

public class TripNotRemovableException extends Exception {

    private Long tripId;

    public TripNotRemovableException(Long tripId) {
        super("Trip with id = " + tripId + " cannot be deleted because it has participants.");
        this.tripId = tripId;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }
}
