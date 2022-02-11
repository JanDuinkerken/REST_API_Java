package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;

public class ClientTripDto {

    private Long tripId;
    private String city;
    private String description;
    private LocalDateTime startDate;
    private float price;
    private int maxParticipants;
    private int freePlaces;

    public ClientTripDto(Long tripId, String city, String description, LocalDateTime startDate, float price, int maxParticipants) {
        this.tripId = tripId;
        this.city = city;
        this.description = description;
        this.startDate = startDate;
        this.price = price;
        this.maxParticipants = maxParticipants;
    }

    public ClientTripDto(String city, String description, LocalDateTime startDate, float price, int maxParticipants) {
        this.city = city;
        this.description = description;
        this.startDate = startDate;
        this.price = price;
        this.maxParticipants = maxParticipants;
    }

    public ClientTripDto(Long tripId, String city, String description, LocalDateTime startDate, float price, int maxParticipants, int freePlaces) {
        this.tripId = tripId;
        this.city = city;
        this.description = description;
        this.startDate = startDate;
        this.price = price;
        this.maxParticipants = maxParticipants;
        this.freePlaces = freePlaces;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
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

    public void setPrice(float price) {
        this.price = price;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getFreePlaces() {
        return freePlaces;
    }

    public void setFreePlaces(int freePlaces) {
        this.freePlaces = freePlaces;
    }

    @Override
    public String toString() {
        return "TripDto [" +
                "tripId=" + tripId +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", price=" + price +
                ", maxParticipants=" + maxParticipants +
                ", freePlaces=" + freePlaces +
                ']';
    }
}
