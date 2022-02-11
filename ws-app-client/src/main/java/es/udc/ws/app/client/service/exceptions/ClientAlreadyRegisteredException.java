package es.udc.ws.app.client.service.exceptions;

public class ClientAlreadyRegisteredException extends Exception {

    private Long registerId;

    public ClientAlreadyRegisteredException(Long registerId) {
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
