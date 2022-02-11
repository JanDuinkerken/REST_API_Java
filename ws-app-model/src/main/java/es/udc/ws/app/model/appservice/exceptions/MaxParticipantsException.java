package es.udc.ws.app.model.appservice.exceptions;

public class MaxParticipantsException extends Exception{

    private Long registerId;
    private int numReserves;
    private int freePlaces;

    public MaxParticipantsException(Long registerId, int numReserves, int freePlaces) {
        super("Reserve with id = " + registerId + " cannot be created because the trip has only "
              + freePlaces + " free spaces. Number of reserves: " + numReserves + " > " + freePlaces);
        this.registerId = registerId;
        this.numReserves = numReserves;
        this.freePlaces = freePlaces;
    }

    public Long getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Long registerId) {
        this.registerId = registerId;
    }

    public int getNumReserves() {
        return numReserves;
    }

    public void setNumReserves(int numReserves) {
        this.numReserves = numReserves;
    }

    public int getFreePlaces() {
        return freePlaces;
    }

    public void setFreePlaces(int freePlaces) {
        this.freePlaces = freePlaces;
    }

}
