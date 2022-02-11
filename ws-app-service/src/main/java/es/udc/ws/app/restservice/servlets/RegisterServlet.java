package es.udc.ws.app.restservice.servlets;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.ws.app.model.appservice.exceptions.*;
import es.udc.ws.app.model.register.Register;
import es.udc.ws.app.restservice.dto.RestRegisterDto;
import es.udc.ws.app.model.appservice.AppServiceFactory;
import es.udc.ws.app.restservice.dto.RegisterToRestRegisterDtoConversor;
import es.udc.ws.app.restservice.json.JsonToRestRegisterDtoConversor;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;

public class RegisterServlet extends RestHttpServletTemplate {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException {

            Long tripId = ServletUtils.getMandatoryParameterAsLong(req,"tripId");
            String email = ServletUtils.getMandatoryParameter(req,"email");
            String creditCard = ServletUtils.getMandatoryParameter(req,"creditCard");
            int numReserves = Integer.parseInt(ServletUtils.getMandatoryParameter(req,"numReserves"));

            Register register = null;

            try{
                Register auxRegister = new Register(tripId, email, numReserves, creditCard, LocalDateTime.now());
                register = AppServiceFactory.getService().addRegister(auxRegister);

                assert register != null;
                RestRegisterDto registerDto = RegisterToRestRegisterDtoConversor.toRestRegisterDto(register);
                String registerUrL = ServletUtils.normalizePath(req.getRequestURL().toString() + "/"+ register.getRegisterId());

                Map<String,String> headers = new HashMap<>(1);
                headers.put("Location", registerUrL);
                ServletUtils.writeServiceResponse(resp,HttpServletResponse.SC_CREATED,JsonToRestRegisterDtoConversor.toObjectNode(registerDto),headers);

            } catch (MaxParticipantsException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toMaxParticipantsException(e) , null);
            } catch (LateRegisterException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toLateRegisterException(e) , null);
            } catch (AlreadyRegisteredException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toAlreadyRegisteredException(e) , null);
            } catch (InstanceNotFoundException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toInstanceNotFoundException(e), null);
            }
    }

    @Override
    protected void processPut(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException {
        Long registerId = ServletUtils.getMandatoryParameterAsLong(req, "registerId");
        String email = ServletUtils.getMandatoryParameter(req, "email");

        try {
            AppServiceFactory.getService().cancelRegister(registerId, email);
        } catch (LateCancelException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toLateCancelException(e), null);
        } catch (AlreadyCancelledException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toAlreadyCancelledException(e), null);
        } catch (DifferentUserException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toDifferentUserException(e), null);
        } catch (InstanceNotFoundException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toInstanceNotFoundException(e), null);
        }

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException {

        ServletUtils.checkEmptyPath(req);
        String email = ServletUtils.getMandatoryParameter(req, "email");

        List<Register> registers = null;

        registers = AppServiceFactory.getService().findRegisters(email);

        List<RestRegisterDto> registerDtos = RegisterToRestRegisterDtoConversor.toRestRegisterDtos(registers);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                JsonToRestRegisterDtoConversor.toArrayNode(registerDtos), null);
    }
}
