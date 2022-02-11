package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.app.thrift.ThriftTripDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientTripDtoToThriftTripDtoConversor {
    public static ThriftTripDto toThriftTripDto(
            ClientTripDto clientTripDto) {

        Long tripId = clientTripDto.getTripId();

        return new ThriftTripDto(
                tripId == null ? -1 : tripId.longValue(),
                clientTripDto.getCity(),
                clientTripDto.getDescription(),
                clientTripDto.getStartDate().toString(),
                clientTripDto.getPrice(),
                clientTripDto.getMaxParticipants(),
                clientTripDto.getFreePlaces());

    }

    public static List<ClientTripDto> toClientTripDto(List<ThriftTripDto> trips) {

        List<ClientTripDto> clientTripDtos = new ArrayList<>(trips.size());

        for (ThriftTripDto trip : trips) {
            clientTripDtos.add(toClientTripDto(trip));
        }
        return clientTripDtos;
    }

    private static ClientTripDto toClientTripDto(ThriftTripDto trip) {

        return new ClientTripDto(
                trip.getTripId(),
                trip.getCity(),
                trip.getDescription(),
                LocalDateTime.parse(trip.getStartDate()),
                Double.valueOf(trip.getPrice()).floatValue(),
                trip.getMaxParticipants(),
                trip.getFreePlaces());
    }
}
