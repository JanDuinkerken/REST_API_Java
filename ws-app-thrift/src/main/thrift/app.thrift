namespace java es.udc.ws.app.thrift

struct ThriftTripDto {
    1: i64 tripId;
    2: string city;
    3: string description;
    4: string startDate;
    5: double price;
    6: i32 maxParticipants;
    7: i32 freePlaces;
}

struct ThriftRegisterDto {
    1: i64 registerId;
    2: i64 tripId;
    3: string email;
    4: i32 numReserves;
    5: string creditCard;
    6: double price;
    7: string registerDate;
    8: string cancelDate;
}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}

exception ThriftAlreadyCancelledException {
    1: i64 registerId
    2: string cancelDate
}

exception ThriftAlreadyRegisteredException {
    1: i64 registerId
}

exception ThriftDifferentUserException {
    1: i64 registerId
    2: string email
}

exception ThriftInvalidDatesException {
    1: string startDate
    2: string finishDate
}

exception ThriftLateCancelException {
    1: i64 registerId
}

exception ThriftLateRegisterException {
    1: i64 registerId
}

exception ThriftLateTripException {
    1: string startDate
}

exception ThriftLateUpdateTripException {
    1: i64 tripId
    2: string startDate
}

exception ThriftMaxParticipantsException{
    1: i64 registerId
    2: i32 numReserves
    3: i32 freePlaces
}

exception ThriftParticipantsUpdateException {
    1: i32 maxParticipants
    2: i32 numParticipants
}

service ThriftAppService {

   ThriftTripDto addTrip(1: ThriftTripDto tripDto) throws (1: ThriftInputValidationException e, 2: ThriftLateTripException ee)

   void updateTrip(1: ThriftTripDto tripDto) throws (1: ThriftInputValidationException e, 2: ThriftInstanceNotFoundException ee,
   3: ThriftLateUpdateTripException eee, 4:  ThriftParticipantsUpdateException eeee)

   list<ThriftTripDto> findTrips(1: string city, 2: string startDate, 3: string finishDate)

   i64 addRegister(1: i64 tripId, 2: string email, 3: i32 numReserves, 4: string creditCard) throws (1: ThriftInputValidationException e)

   void cancelRegister(1: i64 registerId, 2: string email) throws (1: ThriftInputValidationException e, 2: ThriftInstanceNotFoundException ee,
   3: ThriftAlreadyCancelledException eee, 4: ThriftLateCancelException eeee, 5:ThriftDifferentUserException eeeee)

   list<ThriftRegisterDto> findRegister(1: string email)

}