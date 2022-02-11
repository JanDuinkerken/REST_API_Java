package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JsonToClientExceptionConversor {

    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InputValidation")) {
                    return toInputValidationException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InputValidationException toInputValidationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }

    public static Exception fromNotFoundErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }

    public static Exception fromForbiddenErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                switch (errorType) {
                    case "InstanceNotFound": return toInstanceNotFoundException(rootNode);
                    case "AlreadyCancelledException": return toAlreadyCancelledException(rootNode);
                    case "AlreadyRegisteredException": return toAlreadyRegisteredException(rootNode);
                    case "DifferentUserException": return toDifferentUserException(rootNode);
                    case "InvalidDatesException": return toInvalidDatesException(rootNode);
                    case "LateCancelException": return toLateCancelException(rootNode);
                    case "LateRegisterException": return toLateRegisterException(rootNode);
                    case "LateTripException": return toLateTripException(rootNode);
                    case "LateUpdateTripException": return toLateUpdateTripException(rootNode);
                    case "MaxParticipantsException": return toMaxParticipantsException(rootNode);
                    case "ParticipantsUpdateException": return toParticipantsUpdateException(rootNode);
                    default: throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static Exception fromGoneErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                throw new ParsingException("Unrecognized error type: " + errorType);
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientParticipantsUpdateException toParticipantsUpdateException(JsonNode rootNode) {
        int maxParticipants = rootNode.get("maxParticipants").intValue();
        int numParticipants = rootNode.get("numParticipants").intValue();
        return new ClientParticipantsUpdateException(maxParticipants, numParticipants);
    }

    private static ClientMaxParticipantsException toMaxParticipantsException(JsonNode rootNode) {
        Long registerId = rootNode.get("registerId").longValue();
        int numReserves = rootNode.get("numReserves").intValue();
        int freePlaces = rootNode.get("freePlaces").intValue();
        return new ClientMaxParticipantsException(registerId, numReserves, freePlaces);
    }

    private static ClientLateUpdateTripException toLateUpdateTripException(JsonNode rootNode) {
        Long tripId = rootNode.get("tripId").longValue();
        LocalDateTime startDate = LocalDateTime.parse(rootNode.get("startDate").textValue().trim());
        return new ClientLateUpdateTripException(tripId, startDate);
    }

    private static ClientLateTripException toLateTripException(JsonNode rootNode) {
        LocalDateTime startDate = LocalDateTime.parse(rootNode.get("startDate").textValue().trim());
        return new ClientLateTripException(startDate);
    }

    private static ClientLateCancelException toLateCancelException(JsonNode rootNode) {
        Long registerId = rootNode.get("registerId").longValue();
        return new ClientLateCancelException(registerId);
    }

    private static ClientLateRegisterException toLateRegisterException(JsonNode rootNode) {
        Long registerId = rootNode.get("registerId").longValue();
        return new ClientLateRegisterException(registerId);
    }

    private static ClientInvalidDatesException toInvalidDatesException(JsonNode rootNode) {
        LocalDate startDate = LocalDate.parse(rootNode.get("startDate").textValue().trim());
        LocalDate finishDate = LocalDate.parse(rootNode.get("finishDate").textValue().trim());
        return new ClientInvalidDatesException(startDate, finishDate);
    }

    private static ClientDifferentUserException toDifferentUserException(JsonNode rootNode) {
        Long registerId = rootNode.get("registerId").longValue();
        String email = rootNode.get("email").textValue().trim();
        return new ClientDifferentUserException(registerId, email);
    }

    private static ClientAlreadyRegisteredException toAlreadyRegisteredException(JsonNode rootNode) {
        Long registerId = rootNode.get("registerId").longValue();
        return new ClientAlreadyRegisteredException(registerId);
    }

    private static ClientAlreadyCancelledException toAlreadyCancelledException(JsonNode rootNode) {
        Long registerId = rootNode.get("registerId").longValue();
        LocalDateTime cancelDate = LocalDateTime.parse(rootNode.get("cancelDate").textValue().trim());
        return new ClientAlreadyCancelledException(registerId, cancelDate);
    }
}
