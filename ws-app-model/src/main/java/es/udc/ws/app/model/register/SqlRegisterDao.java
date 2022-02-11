package es.udc.ws.app.model.register;

import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

public interface SqlRegisterDao {

    Register addRegister(Connection connection, Register register) throws InstanceNotFoundException;

    Register findRegister(Connection connection, Long registerId) throws InstanceNotFoundException;

    List<Register> findAllUserRegisters(Connection connection, String email);

    void updateRegister(Connection connection, Register register, String email, int numReserves,
                        String creditCard, Float price, Timestamp registerDate, Timestamp cancelDate) throws InstanceNotFoundException;

    void removeRegister(Connection connection, Long registerId) throws InstanceNotFoundException;
}