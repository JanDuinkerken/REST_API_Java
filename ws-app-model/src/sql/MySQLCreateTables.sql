-- ----------------------------------------------------------------------------
-- Model
-- -----------------------------------------------------------------------------

DROP TABLE Register;
DROP TABLE Trip;

-- -----------------------------------------------------------------------------
CREATE TABLE Trip (
    tripId BIGINT NOT NULL AUTO_INCREMENT,
    city VARCHAR(50) NOT NULL,
    description VARCHAR(1024) COLLATE latin1_bin NOT NULL,
    startDate DATETIME NOT NULL,
    price FLOAT NOT NULL,
    maxParticipants SMALLINT NOT NULL,
    freePlaces SMALLINT NOT NULL,
    creationDate DATETIME NOT NULL,
    CONSTRAINT TripPK PRIMARY KEY (tripId),
    CONSTRAINT validTripPrice CHECK ( price >= 0 ),
    CONSTRAINT validMaxParticipants CHECK ( maxParticipants >= 0 ),
    CONSTRAINT  validFreePlaces CHECK ( freePlaces >= 0 && maxParticipants >= Trip.freePlaces)
) ENGINE = InnoDB;

CREATE TABLE Register (
    registerId BIGINT NOT NULL AUTO_INCREMENT,
    tripId BIGINT NOT NULL,
    email VARCHAR(50) NOT NULL,
    numReserves SMALLINT NOT NULL,
    creditCard VARCHAR(16) NOT NULL,
    price FLOAT NOT NULL,
    registerDate DATETIME NOT NULL,
    cancelDate DATETIME,
    CONSTRAINT registerPK PRIMARY KEY (registerId),
    CONSTRAINT tripFK FOREIGN KEY (tripId)
      REFERENCES Trip(tripId) ON DELETE CASCADE,
    CONSTRAINT validNumReserves CHECK ( numReserves >= 0 )
) ENGINE = InnoDB;