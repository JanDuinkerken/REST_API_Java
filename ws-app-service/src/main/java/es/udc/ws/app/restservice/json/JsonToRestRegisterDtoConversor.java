package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestRegisterDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

public class JsonToRestRegisterDtoConversor {

    public static ObjectNode toObjectNode(RestRegisterDto register) {

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

        if (register.getCancelDate() != null) {
            registerObject.put("cancelDate", register.getCancelDate().toString());
        }

        return registerObject;
    }

    public static ArrayNode toArrayNode(List<RestRegisterDto> registers) {

        ArrayNode registersNode = JsonNodeFactory.instance.arrayNode();
        for (RestRegisterDto registerDto : registers) {
            ObjectNode registerObject = toObjectNode(registerDto);
            registersNode.add(registerObject);
        }

        return registersNode;
    }

    public static RestRegisterDto toRestRegisterDto(InputStream jsonRegister) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonRegister);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode registerObject = (ObjectNode) rootNode;

                JsonNode registerIdNode = registerObject.get("RegisterId");
                Long registerId = (registerIdNode != null) ? registerIdNode.longValue() : null;

                Long tripId = registerObject.get("tripId").longValue();
                String email = registerObject.get("email").textValue().trim();
                int numReserves = registerObject.get("numReserves").intValue();
                String creditCard = registerObject.get("creditCard").textValue().trim();
                float price = registerObject.get("price").floatValue();
                String registerDate_s = registerObject.get("registerDate").textValue().trim();
                String cancelDate_s = registerObject.get("cancelDate").textValue().trim();

                LocalDateTime registerDate = LocalDateTime.parse(registerDate_s);
                LocalDateTime cancelDate = LocalDateTime.parse(cancelDate_s);

                return new RestRegisterDto(registerId, tripId, email, numReserves, creditCard, price, registerDate, cancelDate);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
