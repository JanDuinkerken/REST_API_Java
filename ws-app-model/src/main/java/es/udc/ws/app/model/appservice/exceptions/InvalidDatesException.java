package es.udc.ws.app.model.appservice.exceptions;

import java.time.LocalDate;

public class InvalidDatesException extends Exception{

    private LocalDate startDate;
    private LocalDate finishDate;

    public InvalidDatesException(LocalDate startDate, LocalDate finishDate){
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
