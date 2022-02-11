package es.udc.ws.app.client.service.exceptions;

import static es.udc.ws.app.client.service.util.ModelConstants.ADD_RESERVE_MIN_HOURS_START;

public class ClientLateRegisterException extends Exception{

    private Long registerId;

    public ClientLateRegisterException(Long registerId) {
        super("Reserve with id = " + registerId + " cannot be created because the trip starts in less than " +
                ADD_RESERVE_MIN_HOURS_START + " hours.");
        this.registerId = registerId;
    }

    public Long getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Long registerId) {
        this.registerId = registerId;
    }
}
