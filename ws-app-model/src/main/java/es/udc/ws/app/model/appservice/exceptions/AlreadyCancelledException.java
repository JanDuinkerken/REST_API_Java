package es.udc.ws.app.model.appservice.exceptions;

import java.time.LocalDateTime;

public class AlreadyCancelledException extends Exception {

    private Long registerId;
    private LocalDateTime cancelDate;

    public AlreadyCancelledException(Long registerId, LocalDateTime cancelDate) {
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
