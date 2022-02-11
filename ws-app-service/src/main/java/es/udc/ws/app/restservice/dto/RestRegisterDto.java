package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;

public class RestRegisterDto {
    private Long registerId;
    private Long tripId;
    private String email;
    private int numReserves;
    private String creditCard;
    private float price;
    private LocalDateTime registerDate;
    private LocalDateTime cancelDate = null;

    public RestRegisterDto(Long registerId, Long tripId, String email, int numReserves, String creditCard,
                           float price, LocalDateTime registerDate, LocalDateTime cancelDate) {
        this.registerId = registerId;
        this.tripId = tripId;
        this.email = email;
        this.numReserves = numReserves;
        this.creditCard = creditCard;
        this.price = price;
        this.registerDate = registerDate;
        this.cancelDate = cancelDate;
    }

    public Long getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Long registerId) {
        this.registerId = registerId;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNumReserves() {
        return numReserves;
    }

    public void setNumReserves(int numReserves) {
        this.numReserves = numReserves;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public LocalDateTime getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDateTime cancelDate) {
        this.cancelDate = cancelDate;
    }

    @Override
    public String toString() {
        return "RestRegisterDto[" +
                "registerId=" + registerId +
                ", tripId=" + tripId +
                ", email='" + email + '\'' +
                ", numReserves=" + numReserves +
                ", creditCard='" + creditCard + '\'' +
                ", price=" + price +
                ", registerDate=" + registerDate +
                ", cancelDate=" + cancelDate +
                ']';
    }
}
