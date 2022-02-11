package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientRegisterDto;
import es.udc.ws.app.thrift.ThriftRegisterDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientRegisterDtoToThriftRegisterDtoConversor {
    public static ThriftRegisterDto toThriftRegisterDto(
            ClientRegisterDto clientRegisterDto) {

        Long registerId = clientRegisterDto.getRegisterId();

        String cancelDate = (clientRegisterDto.getCancelDate() !=null) ? clientRegisterDto.getCancelDate().toString() : null;

        return new ThriftRegisterDto(
                registerId == null ? -1 : registerId.longValue(),
                clientRegisterDto.getTripId(),
                clientRegisterDto.getEmail(),
                clientRegisterDto.getNumReserves(),
                clientRegisterDto.getCreditCard(),
                clientRegisterDto.getPrice(),
                clientRegisterDto.getRegisterDate().toString(),
                cancelDate);
    }

    public static List<ClientRegisterDto> toClientRegisterDtos(List<ThriftRegisterDto> registers) {

        List<ClientRegisterDto> clientRegisterDtos = new ArrayList<>(registers.size());

        for (ThriftRegisterDto register : registers) {
            clientRegisterDtos.add(toClientRegisterDto(register));
        }
        return clientRegisterDtos;
    }

    private static ClientRegisterDto toClientRegisterDto(ThriftRegisterDto register) {

        LocalDateTime registerDate = LocalDateTime.parse(register.registerDate);
        LocalDateTime cancelDate = (register.getCancelDate() != null) ? LocalDateTime.parse(register.registerDate) : null;

        return new ClientRegisterDto(
                register.getRegisterId(),
                register.getTripId(),
                register.getEmail(),
                register.getNumReserves(),
                register.getCreditCard(),
                Double.valueOf(register.getPrice()).floatValue(),
                registerDate,
                cancelDate);
    }
}
