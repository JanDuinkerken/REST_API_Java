package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientAppService;
import es.udc.ws.app.client.service.ClientAppServiceFactory;
import es.udc.ws.app.client.service.rest.RestClientAppService;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.app.client.service.dto.ClientRegisterDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AppServiceClient {

    public static void main(String[] args) {

        if(args.length == 0) {
            printUsageAndExit();
        }
        ClientAppService clientAppService = ClientAppServiceFactory.getService();

        if("-addTrip".equalsIgnoreCase(args[0])) {
            validateArgs(args, 6, new int[] {4, 5});
            //[addTrip] -addTrip <city> <description> <startDate> <price> <maxParticipants>

            try {
                Long tripId = clientAppService.addTrip(new ClientTripDto(args[1],
                        args[2], LocalDateTime.parse(args[3]),
                        Float.valueOf(args[4]), Integer.valueOf(args[5])));

                System.out.println("Trip " + tripId + " created sucessfully");

            } catch (NumberFormatException | InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if("-updateTrip".equalsIgnoreCase(args[0])) {
            validateArgs(args, 7, new int[] {1, 5, 6});
            // [updateTrip] -updateTrip <tripId> <city> <description> <startDate> <price> <maxParticipants>

            try {
                clientAppService.updateTrip(new ClientTripDto(
                        Long.valueOf(args[1]),
                        args[2], args[3], LocalDateTime.parse(args[4]),
                        Float.valueOf(args[5]), Integer.valueOf(args[6])));

                System.out.println("Trip " + args[1] + " updated sucessfully");

            } catch (NumberFormatException | InputValidationException |
                    InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if("-findTrips".equalsIgnoreCase(args[0])) {
            validateArgs(args, 4, new int[] {});
            // [findTrips] -findTrips <city> <startDate> <finishDate>

            try {
                List<ClientTripDto> trips = clientAppService.findTrips(args[1], LocalDate.parse(args[2]), LocalDate.parse(args[3]));
                System.out.println("Found " + trips.size() + " trip(s) in " + args[1] + " between " + args[2] + " and " + args[3] + "\n");
                for (ClientTripDto tripDto : trips) {
                    System.out.println("Trip with Id: " + tripDto.getTripId() + "\n" +
                            "  Description: " + tripDto.getDescription() +
                            ", Date: " + tripDto.getStartDate() +
                            ", Occupied places: " + (tripDto.getMaxParticipants() - tripDto.getFreePlaces()) +
                            ", Total places: " + tripDto.getMaxParticipants() +
                            ", Price: " + tripDto.getPrice());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-addRegister".equalsIgnoreCase(args[0])) {
            validateArgs(args, 5, new int[] {1, 3});
            // [addRegister] -addRegister <tripId> <email> <numReserves> <creditCard>

            try {
                Long registerId = clientAppService.addRegister(Long.valueOf(args[1]), args[2],
                Integer.valueOf(args[3]), args[4]);

                System.out.println("Register " + registerId + " created sucessfully");
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-cancelRegister".equalsIgnoreCase(args[0])) {
            validateArgs(args, 3, new int[] {1});
            // [cancelRegister] -cancelRegister <registerId> <email>

            try {
                clientAppService.cancelRegister(Long.valueOf(args[1]), args[2]);

                System.out.println("Resgister " + args[1] + " canceled sucessfully");

            } catch (NumberFormatException | InputValidationException | InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }


        } else if("-findRegisters".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {});
            // [[findRegisters] -findRegisters <email>

            try {
                List<ClientRegisterDto> registers = clientAppService.findRegister(args[1]);
                System.out.println("Found " + registers.size() + " register(s) with " + args[1] + " as the email\n");
                boolean cancel = false;
                for (ClientRegisterDto registerDto : registers) {
                    cancel = registerDto.getCancelDate() != null;
                    System.out.println("Register with Id: " + registerDto.getRegisterId() + "\n" +
                            "  Trip Id: " + registerDto.getTripId() +
                            ", Email: " + registerDto.getEmail() +
                            ", Number of Reserves: " + registerDto.getNumReserves() +
                            ", Credit Card: " + registerDto.getCreditCard() +
                            ", Price: " + registerDto.getPrice() +
                            ", Register Date: " + registerDto.getRegisterDate() +
                            ", Cancel: " + cancel);
                }

        } catch (InputValidationException e) {
                e.printStackTrace();
            }
        }
    }

    public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {
        if(expectedArgs != args.length) {
            printUsageAndExit();
        }
        for (int position : numericArguments) {
            try {
                Double.parseDouble(args[position]);
            } catch (NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [addTrip]         -addTrip <city> <description> <startDate> <price> <maxParticipants>\n" +
                "    [updateTrip]      -updateTrip <tripId> <city> <description> <startDate> <price> <maxParticipants>\n" +
                "    [findTrips]       -findTrips <city> <startDate> <finishDate>\n" +
                "    [addRegisters]    -addRegister <tripId> <email> <numReserves> <creditCard>\n" +
                "    [cancelRegister]  -cancelRegister <registerId> <email>\n" +
                "    [findRegisters]    -findRegisters <email>\n");
    }
}