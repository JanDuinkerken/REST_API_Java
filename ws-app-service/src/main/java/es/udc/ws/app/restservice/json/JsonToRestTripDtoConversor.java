package es.udc.ws.app.restservice.json;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.restservice.dto.RestTripDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonToRestTripDtoConversor {

    public static ObjectNode toObjectNode(RestTripDto trip) {

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

	public static ArrayNode toArrayNode(List<RestTripDto> trips) {

		ArrayNode tripsNode = JsonNodeFactory.instance.arrayNode();
		for (RestTripDto tripDto : trips) {
			ObjectNode tripObject = toObjectNode(tripDto);
			tripsNode.add(tripObject);
		}

		return tripsNode;
	}

	public static RestTripDto toRestTripDto(InputStream jsonTrip) throws ParsingException {
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonTrip);
			
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				ObjectNode tripObject = (ObjectNode) rootNode;

				JsonNode tripIdNode = tripObject.get("tripId");
				Long tripId = (tripIdNode != null) ? tripIdNode.longValue() : null;

                String city = tripObject.get("city").textValue().trim();
				String description = tripObject.get("description").textValue().trim();
                String startDate = tripObject.get("startDate").textValue().trim();
				float price = tripObject.get("price").floatValue();
                int maxParticipants = tripObject.get("maxParticipants").intValue();
				int freePlaces = tripObject.get("freePlaces").intValue();

				return new RestTripDto(tripId, city, description, LocalDateTime.parse(startDate), price, maxParticipants, freePlaces);
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
}
