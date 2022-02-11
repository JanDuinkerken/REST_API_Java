package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientRegisterDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientRegisterDtoConversor {

    public static ObjectNode toObjectNode(ClientRegisterDto register) {

        ObjectNode registerObject = JsonNodeFactory.instance.objectNode();

        if (register.getRegisterId() != null) {
            registerObject.put("registerId", register.getRegisterId());
        }
        registerObject.put("tripId", register.getTripId()).
                put("email", register.getEmail()).
                put("numReserves", register.getNumReserves()).
                put("creditCard", register.getCreditCard()).
                put("price", register.getPrice()).
                put("registerDate", register.getRegisterDate().toString());
        if(register.getCancelDate() != null) {
            registerObject.put("cancelDate", register.getCancelDate().toString());
        }
        return registerObject;
    }

    public static ArrayNode toArrayNode(List<ClientRegisterDto> registers) {

        ArrayNode registersNode = JsonNodeFactory.instance.arrayNode();
        for (ClientRegisterDto registerDto : registers) {
            ObjectNode registerObject = toObjectNode(registerDto);
            registersNode.add(registerObject);
        }

        return registersNode;
    }

    public static ClientRegisterDto toClientRegisterDto(InputStream jsonRegister) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonRegister);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientRegisterDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientRegisterDto> toClientRegisterDtos(InputStream jsonRegisters) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonRegisters);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode registersArray = (ArrayNode) rootNode;
                List<ClientRegisterDto> registerDtos = new ArrayList<>(registersArray.size());
                for (JsonNode registerNode : registersArray) {
                    registerDtos.add(toClientRegisterDto(registerNode));
                }

                return registerDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientRegisterDto toClientRegisterDto(JsonNode registerNode) throws ParsingException {
        if (registerNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode registerObject = (ObjectNode) registerNode;

            JsonNode registerIdNode = registerObject.get("registerId");
            Long registerId = (registerIdNode != null) ? registerIdNode.longValue() : null;

            Long tripId = registerObject.get("tripId").longValue();
            String email = registerObject.get("email").textValue().trim();
            String creditCard = registerObject.get("creditCard").textValue().trim();
            float price = registerObject.get("price").floatValue();
            int numReserves = registerObject.get("numReserves").intValue();
            LocalDateTime registerDate = LocalDateTime.parse(registerObject.get("registerDate").textValue().trim());
            if(registerObject.get("cancelDate") != null) {
                LocalDateTime cancelRegister = LocalDateTime.parse(registerObject.get("cancelDate").textValue().trim());

                return new ClientRegisterDto(registerId, tripId, email, numReserves, creditCard, price, registerDate, cancelRegister);
            } else {
                return new ClientRegisterDto(registerId, tripId, email, numReserves, creditCard, price, registerDate);
            }
        }
    }
}
