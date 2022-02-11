package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;

public class RestTripDto {
    private Long tripId;
    private String city;
    private String description;
    private LocalDateTime startDate;
    private float price;
    private int maxParticipants;
    private int freePlaces;

    public RestTripDto(Long tripId, String city, String description, LocalDateTime startDate, float price,
                       int maxParticipants){
        this.tripId = tripId;
        this.city = city;
        this.description = description;
        this.startDate = startDate;
        this.price = price;
        this.maxParticipants = maxParticipants;
    }

    public RestTripDto(Long tripId, String city, String description, LocalDateTime startDate, float price,
                       int maxParticipants, int freePlaces){
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

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Integer getFreePlaces() {
        return freePlaces;
    }

    public void setFreePlaces(Integer freePlaces) {
        this.freePlaces = freePlaces;
    }

    @Override
    public String toString() {
        return "RestTripDto[" +
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
