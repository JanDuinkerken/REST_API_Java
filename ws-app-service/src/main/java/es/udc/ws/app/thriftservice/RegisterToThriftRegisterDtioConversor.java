package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.register.Register;
import es.udc.ws.app.thrift.ThriftRegisterDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RegisterToThriftRegisterDtioConversor {
    public static Register toRegister(ThriftRegisterDto register) {
        return new Register(register.getRegisterId(), register.getTripId(), register.getEmail(),
                register.getNumReserves(), register.getCreditCard(), Double.valueOf(register.getPrice()).floatValue(),
                LocalDateTime.parse(register.getRegisterDate()));
    }

    public static List<ThriftRegisterDto> toThriftRegisterDto(List<Register> registers) {

        List<ThriftRegisterDto> dtos = new ArrayList<>(registers.size());

        for (Register register : registers) {
            dtos.add(toThriftRegisterDto(register));
        }
        return dtos;

    }

    public static ThriftRegisterDto toThriftRegisterDto(Register register) {

        String cancelDate = (register.getCancelDate() != null) ? register.getCancelDate().toString() : null;

        return new ThriftRegisterDto(
                register.getRegisterId(),
                register.getTripId(),
                register.getEmail(),
                register.getNumReserves(),
                register.getCreditCard().substring(register.getCreditCard().length()-4),
                register.getPrice(),
                register.getRegisterDate().toString(),
                cancelDate);


    }
}
