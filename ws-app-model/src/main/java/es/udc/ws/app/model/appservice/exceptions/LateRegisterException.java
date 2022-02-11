package es.udc.ws.app.model.appservice.exceptions;

import static es.udc.ws.app.model.util.ModelConstants.ADD_RESERVE_MIN_HOURS_START;

public class LateRegisterException extends Exception{

    private Long registerId;

    public LateRegisterException(Long registerId) {
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
