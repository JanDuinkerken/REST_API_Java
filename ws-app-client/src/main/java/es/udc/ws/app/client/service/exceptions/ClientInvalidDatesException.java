package es.udc.ws.app.client.service.exceptions;

import java.time.LocalDate;

public class ClientInvalidDatesException extends Exception{

    private LocalDate startDate;
    private LocalDate finishDate;

    public ClientInvalidDatesException(LocalDate startDate, LocalDate finishDate){
        super("Cannot search trips because the startDate: " + startDate
                        + " is later than finishDate: " + finishDate);
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }
}
