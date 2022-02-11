package es.udc.ws.app.client.service.rest.json;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonToClientTripDtoConversor {

    public static ObjectNode toObjectNode(ClientTripDto trip) throws IOException {

        ObjectNode tripObject = JsonNodeFactory.instance.objectNode();

        if (trip.getTripId() != null) {
            tripObject.put("tripId", trip.getTripId());
        }
        tripObject.put("city", trip.getCity()).
                put("description", trip.getDescription()).
                put("startDate", trip.getStartDate().toString()).
                put("price", trip.getPrice()).
                put("maxParticipants", trip.getMaxParticipants()).
                put("freePlaces", trip.getFreePlaces());

        return tripObject;
    }

    public static ClientTripDto toClientTripDto(InputStream jsonTrip) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonTrip);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientTripDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientTripDto> toClientTripDtos(InputStream jsonTrips) throws ParsingException {

        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonTrips);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode tripsArray = (ArrayNode) rootNode;
                List<ClientTripDto> tripDtos = new ArrayList<>(tripsArray.size());
                for (JsonNode tripNode : tripsArray) {
                    tripDtos.add(toClientTripDto(tripNode));
                }

                return tripDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientTripDto toClientTripDto(JsonNode tripNode) throws ParsingException {
        if (tripNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode tripObject = (ObjectNode) tripNode;

            JsonNode tripIdNode = tripObject.get("tripId");
            Long tripId = (tripIdNode != null) ? tripIdNode.longValue() : null;

            String city = tripObject.get("city").textValue().trim();
            String description = tripObject.get("description").textValue().trim();
            String startDate = tripObject.get("startDate").textValue().trim();
            float price = tripObject.get("price").floatValue();
            int maxParticipants = tripObject.get("maxParticipants").intValue();
            int freePlaces = tripObject.get("freePlaces").intValue();

            return new ClientTripDto(tripId, city, description, LocalDateTime.parse(startDate), price, maxParticipants, freePlaces);
        }
    }
}
