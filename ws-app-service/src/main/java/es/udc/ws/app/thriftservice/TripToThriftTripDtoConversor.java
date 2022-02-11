package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.thrift.ThriftTripDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TripToThriftTripDtoConversor {
    public static Trip toTrip(ThriftTripDto trip) {
        return new Trip(
                trip.getTripId(),
                trip.getCity(),
                trip.getDescription(),
                LocalDateTime.parse(trip.getStartDate()),
                Double.valueOf(trip.getPrice()).floatValue(),
                trip.getMaxParticipants(),
                trip.getMaxParticipants() - trip.getFreePlaces());
    }

    public static List<ThriftTripDto> toThriftTripDtos(List<Trip> trips) {

        List<ThriftTripDto> dtos = new ArrayList<>(trips.size());

        for (Trip trip : trips) {
            dtos.add(toThriftTripDto(trip));
        }
        return dtos;

    }

    public static ThriftTripDto toThriftTripDto(Trip trip) {

        return new ThriftTripDto(
                trip.getTripId(),
                trip.getCity(),
                trip.getDescription(),
                trip.getStartDate().toString(),
                trip.getPrice(),
                trip.getMaxParticipants(),
                trip.getFreePlaces());

    }
}
