package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.appservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class AppExceptionToJsonConversor {

    public static ObjectNode toInstanceNotFoundException(InstanceNotFoundException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "InstanceNotFound");
        exceptionObject.put("instanceId", (ex.getInstanceId() != null) ?
                ex.getInstanceId().toString() : null);
        exceptionObject.put("instanceType",
                ex.getInstanceType().substring(ex.getInstanceType().lastIndexOf('.') + 1));

        return exceptionObject;
    }

    public static ObjectNode toAlreadyCancelledException(AlreadyCancelledException ex){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "AlreadyCancelledException");
        exceptionObject.put("registerId", (ex.getRegisterId() != null) ? ex.getRegisterId() : null);
        if (ex.getCancelDate() != null) {
            exceptionObject.put("cancelDate", ex.getCancelDate().toString());
        } else {
            exceptionObject.set("cancelDate", null);
        }

        return exceptionObject;
    }

    public static ObjectNode toAlreadyRegisteredException(AlreadyRegisteredException ex){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "AlreadyRegisteredException");
        exceptionObject.put("registerId", (ex.getRegisterId() != null) ? ex.getRegisterId() : null);

        return exceptionObject;
    }

    public static ObjectNode toDifferentUserException(DifferentUserException ex){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "DifferentUserException");
        exceptionObject.put("registerId", (ex.getRegisterId() != null) ? ex.getRegisterId() : null);
        exceptionObject.put("email", (ex.getEmail() != null) ? ex.getEmail() : null);

        return exceptionObject;
    }

    public static ObjectNode toInvalidDatesException(InvalidDatesException ex){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "InvalidDatesException");
        if (ex.getStartDate() != null) {
            exceptionObject.put("startDate", ex.getStartDate().toString());
        } else {
            exceptionObject.set("startDate", null);
        }
        if (ex.getFinishDate() != null) {
            exceptionObject.put("finishDate", ex.getFinishDate().toString());
        } else {
            exceptionObject.set("finishDate", null);
        }

        return exceptionObject;
    }

    public static ObjectNode toLateCancelException(LateCancelException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "LateCancelException");
        exceptionObject.put("registerId", (ex.getRegisterId() != null) ? ex.getRegisterId() : null);
        

        return exceptionObject;
    }

    public static ObjectNode toLateRegisterException(LateRegisterException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "LateRegisterException");
        exceptionObject.put("registerId", (ex.getRegisterId() != null) ? ex.getRegisterId() : null);


        return exceptionObject;
    }

    public static ObjectNode toLateTripException(LateTripException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "LateTripException");
        exceptionObject.put("startDate", ex.getStartDate().toString());

        return exceptionObject;
    }

    public static ObjectNode toLateUpdateTripException(LateUpdateTripException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "LateUpdateTripException");
        exceptionObject.put("tripId", (ex.getTripId() != null) ? ex.getTripId() : null);
        if (ex.getStartDate() != null) {
            exceptionObject.put("startDate", ex.getStartDate().toString());
        } else {
            exceptionObject.set("startDate", null);
        }

        return exceptionObject;
    }

    public static ObjectNode toMaxParticipantsException(MaxParticipantsException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "MaxParticipantsException");
        exceptionObject.put("registerId", (ex.getRegisterId() != null) ? ex.getRegisterId() : null);
        exceptionObject.put("numReserves", ex.getNumReserves());
        exceptionObject.put("freePlaces", ex.getFreePlaces());

        return exceptionObject;
    }

    public static ObjectNode toParticipantsUpdateException(ParticipantsUpdateException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "ParticipantsUpdateException");
        exceptionObject.put("maxParticipants", ex.getMaxParticipants());
        exceptionObject.put("numParticipants", ex.getNumParticipants());

        return exceptionObject;
    }

    public static ObjectNode toTripNotRemovableException(TripNotRemovableException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "TripNotRemovableException");
        exceptionObject.put("registerId", (ex.getTripId() != null) ? ex.getTripId() : null);

        return exceptionObject;
    }
}

