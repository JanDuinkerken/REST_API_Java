package es.udc.ws.app.model.trip;

import java.time.LocalDateTime;
import java.util.Objects;

public class Trip {
    private Long tripId;
    private String city;
    private String description;
    private LocalDateTime startDate;
    private float price;
    private Integer maxParticipants;
    private Integer numParticipants = 0;
    private LocalDateTime creationDate;

    public Trip(String city, String description, LocalDateTime startDate, float price, int maxParticipants){
        this.city = city;
        this.description = description;
        this.startDate = startDate;
        this.price = price;
        this.maxParticipants = maxParticipants;
    }
    public Trip(Long tripId, String city, String description, LocalDateTime startDate, float price, int maxParticipants){
        this(city, description, startDate, price, maxParticipants);
        this.tripId = tripId;
    }
    public Trip(Long tripId, String city, String description, LocalDateTime startDate, float price, int maxParticipants, LocalDateTime creationDate){
        this(tripId, city, description, startDate, price, maxParticipants);
        this.creationDate = (creationDate != null) ? creationDate.withNano(0) : null;
    }

    public Trip(Long tripId, String city, String description, LocalDateTime startDate, float price, int maxParticipants, int numParticipants, LocalDateTime creationDate){
        this(tripId, city, description, startDate, price, maxParticipants);
        this.numParticipants = numParticipants;
        this.creationDate = (creationDate != null) ? creationDate.withNano(0) : null;
    }

    public Trip(Long tripId, String city, String description, LocalDateTime startDate, float price, int maxParticipants, int numParticipants){
        this(tripId, city, description, startDate, price, maxParticipants);
        this.numParticipants = numParticipants;
        this.creationDate = null;
    }

    public int getFreePlaces(){ return maxParticipants - numParticipants; }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getNumParticipants() {
        return numParticipants;
    }

    public void setNumParticipants(Integer numParticipants) {
        this.numParticipants = numParticipants;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = (creationDate != null) ? creationDate.withNano(0) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trip)) return false;
        Trip trip = (Trip) o;
        return getTripId() == trip.getTripId() && Float.compare(trip.getPrice(), getPrice()) == 0 && Objects.equals(getCity(), trip.getCity()) && Objects.equals(getDescription(), trip.getDescription()) && Objects.equals(getStartDate(), trip.getStartDate()) && Objects.equals(getMaxParticipants(), trip.getMaxParticipants()) && Objects.equals(getNumParticipants(), trip.getNumParticipants()) && Objects.equals(getCreationDate(), trip.getCreationDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTripId(), getCity(), getDescription(), getStartDate(), getPrice(), getMaxParticipants(), getNumParticipants(), getCreationDate());
    }
}
