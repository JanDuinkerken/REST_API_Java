package es.udc.ws.app.model.register;
import java.time.LocalDateTime;
import java.util.Objects;

public class Register {

    private Long registerId;
    private Long tripId;
    private String email;
    private int numReserves;
    private String creditCard;
    private float price;
    private LocalDateTime registerDate;
    private LocalDateTime cancelDate = null;

    public Register(Long tripId, String email, int numReserves, String creditCard) {
        this.tripId = tripId;
        this.email = email;
        this.numReserves = numReserves;
        this.creditCard = creditCard;
    }

    public Register(Long tripId, String email, int numReserves, String creditCard, float price) {
        this.tripId = tripId;
        this.email = email;
        this.numReserves = numReserves;
        this.creditCard = creditCard;
        this.price = price;
    }

    public Register(Long tripId, String email, int numReserves, String creditCard, LocalDateTime registerDate) {
        this.tripId = tripId;
        this.email = email;
        this.numReserves = numReserves;
        this.creditCard = creditCard;
        this.registerDate = registerDate;
    }

    public Register(Long registerId, Long tripId, String email, int numReserves, String creditCard, float price, LocalDateTime registerDate) {
        this(tripId, email, numReserves, creditCard, price);
        this.registerId = registerId;
        this.registerDate = (registerDate != null) ? registerDate.withNano(0) : null;
    }

    public Register(Long registerId, Long tripId, String email, int numReserves, String creditCard, LocalDateTime registerDate,
                    float price, LocalDateTime cancelDate) {
        this(registerId, tripId, email, numReserves, creditCard, price, registerDate);
        this.cancelDate = (cancelDate != null) ? cancelDate.withNano(0) : null;
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
        this.registerDate = (registerDate != null) ? registerDate.withNano(0) : null;
    }

    public LocalDateTime getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDateTime cancelDate) {
        this.cancelDate = cancelDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Register register = (Register) o;
        return numReserves == register.numReserves && Float.compare(register.price, price) == 0 && registerId.equals(register.registerId) && tripId.equals(register.tripId) && email.equals(register.email) && creditCard.equals(register.creditCard) && registerDate.equals(register.registerDate) && Objects.equals(cancelDate, register.cancelDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registerId, tripId, email, numReserves, creditCard, price, registerDate, cancelDate);
    }
}
