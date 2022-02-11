package es.udc.ws.app.client.service.exceptions;

public class ClientDifferentUserException extends Exception{

    private Long registerId;
    private String email;

    public ClientDifferentUserException(Long registerId, String email) {
        super("Reserve with id = " + registerId + " cannot be cancelled because " + email + " is different from the associated email.");
        this.registerId = registerId;
        this.email = email;
    }

    public Long getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Long registerId) {
        this.registerId = registerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
