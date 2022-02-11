package es.udc.ws.app.client.service.exceptions;

import static es.udc.ws.app.client.service.util.ModelConstants.CANCEL_RESERVE_MIN_HOURS_START;

public class ClientLateCancelException extends Exception {

    private Long registerId;

    public ClientLateCancelException(Long registerId) {
        super("Reserve with id = " + registerId + " cannot be cancelled because the trip starts in less than "
                + CANCEL_RESERVE_MIN_HOURS_START + " hours.");
        this.registerId = registerId;
    }

    public Long getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Long registerId) {
        this.registerId = registerId;
    }
}
