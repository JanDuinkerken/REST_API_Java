package es.udc.ws.app.model.appservice.exceptions;

public class ParticipantsUpdateException extends Exception {
    private int numParticipants;
    private int maxParticipants;

    public ParticipantsUpdateException(int maxParticipants, int numParticipants) {
        super("Max participants: " + maxParticipants + "cannot be less than the curren number of participants: " + numParticipants);
        this.numParticipants = numParticipants;
        this.maxParticipants = maxParticipants;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Integer getNumParticipants() {
        return numParticipants;
    }

    public void setNumParticipants(int numParticipants) {
        this.numParticipants = numParticipants;
    }
}
