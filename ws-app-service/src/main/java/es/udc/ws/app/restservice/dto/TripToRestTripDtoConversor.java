package es.udc.ws.app.restservice.dto;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.model.trip.Trip;

public class TripToRestTripDtoConversor {
    public static List<RestTripDto> toRestTripDtos(List<Trip> trips) {
        List<RestTripDto> tripDtos = new ArrayList<>(trips.size());
        for (Trip trip : trips) {
            tripDtos.add(toRestTripDto(trip));
        }
        return tripDtos;
    }

    public static RestTripDto toRestTripDto(Trip trip) {
        return new RestTripDto(trip.getTripId(), trip.getCity(), trip.getDescription(), trip.getStartDate(),
                trip.getPrice(), trip.getMaxParticipants(), trip.getFreePlaces());
    }

    public static Trip toTrip(RestTripDto trip) {
        return new Trip( trip.getTripId(), trip.getCity(), trip.getDescription(), trip.getStartDate(),
                trip.getPrice(), trip.getMaxParticipants(), trip.getMaxParticipants()-trip.getFreePlaces());
    }
}
