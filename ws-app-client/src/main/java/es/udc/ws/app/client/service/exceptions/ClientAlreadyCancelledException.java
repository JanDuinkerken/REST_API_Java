package es.udc.ws.app.client.service.exceptions;

import java.time.LocalDateTime;

public class ClientAlreadyCancelledException extends Exception {

    private Long registerId;
    private LocalDateTime cancelDate;

    public ClientAlreadyCancelledException(Long registerId, LocalDateTime cancelDate) {
        super("Reserve with id = " + registerId + " cannot be cancelled because it has already been cancelled on "
                + cancelDate);
        this.registerId = registerId;
        this.cancelDate = cancelDate;
    }

    public Long getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Long registerId) {
        this.registerId = registerId;
    }

    public LocalDateTime getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDateTime cancelDate) {
        this.cancelDate = cancelDate;
    }
}
