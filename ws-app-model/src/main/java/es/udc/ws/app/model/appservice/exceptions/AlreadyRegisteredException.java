package es.udc.ws.app.model.appservice.exceptions;

public class AlreadyRegisteredException extends Exception {

    private Long registerId;

    public AlreadyRegisteredException(Long registerId) {
        super("Reserve with id = " + registerId + " cannot be created because it already exists.");
        this.registerId = registerId;
    }

    public Long getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Long registerId) {
        this.registerId = registerId;
    }
}
