package es.udc.ws.app.restservice.dto;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.model.register.Register;

public class RegisterToRestRegisterDtoConversor {

    public static List<RestRegisterDto> toRestRegisterDtos(List<Register> registers) {
        List<RestRegisterDto> registerDtos = new ArrayList<>(registers.size());
        for (Register register : registers) {
            registerDtos.add(toRestRegisterDto(register));
        }
        return registerDtos;
    }

    public static RestRegisterDto toRestRegisterDto(Register register) {
        return new RestRegisterDto(register.getRegisterId(), register.getTripId(), register.getEmail(), register.getNumReserves(),
                register.getCreditCard().substring(register.getCreditCard().length()-4), register.getPrice(), register.getRegisterDate(), register.getCancelDate());
    }
}
