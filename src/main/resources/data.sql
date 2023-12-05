INSERT INTO users(email, password, firstName, lastName) VALUES('kevinjames@mallcop.com', 'segway', 'Kevin', 'James');
INSERT INTO users(email, password, firstName, lastName) VALUES('djkhaled@gmail.com', 'anothaOne', 'DJ', 'Khaled');
INSERT INTO users(email, password, firstName, lastName) VALUES('kanye@west.org', 'buyDaEarf123', 'Kanye', 'West');
INSERT INTO users(email, password, firstName, lastName) VALUES('lebron@king.net', 'Strive4Gr8ness', 'LeBron', 'James');
INSERT INTO users(email, password, firstName, lastName) VALUES('aubreyg@yahoo.com', 'money4fun', 'Aubrey', 'Graham');
INSERT INTO users(email, password, firstName, lastName) VALUES('curry@chef.com', 'splash', 'Chef', 'Curry');
INSERT INTO users(email, password, firstName, lastName) VALUES('tswift@eras.com', 'scootersuckz', 'Taylor', 'Swift');

INSERT INTO vehicles(licensePlate, passengerSeats, ownerEmail) VALUES('ABC123', 5, 'kevinjames@mallcop.com');
INSERT INTO vehicles(licensePlate, passengerSeats, ownerEmail) VALUES('XYZ456', 7, 'djkhaled@gmail.com');
INSERT INTO vehicles(licensePlate, passengerSeats, ownerEmail) VALUES('JKL789', 3, 'kanye@west.org');
INSERT INTO vehicles(licensePlate, passengerSeats, ownerEmail) VALUES('MNO012', 3, 'lebron@king.net');
INSERT INTO vehicles(licensePlate, passengerSeats, ownerEmail) VALUES('PQR345', 3, 'aubreyg@yahoo.com');
INSERT INTO vehicles(licensePlate, make, model, type, passengerSeats, color, ownerEmail) VALUES('TS1989', 'Mercedes', 'Maybach S650', 'limo', 4, 'black', 'tswift@eras.com');
INSERT INTO vehicles(licensePlate, make, model, passengerSeats, ownerEmail) VALUES('JFK63', 'Lincoln', 'Continental', 2, 'kevinjames@mallcop.com');

INSERT INTO trips(
                  driverEmail,
                  licensePlate,
                  sunday,
                  monday,
                  tuesday,
                  wednesday,
                  thursday,
                  friday,
                  saturday,
                  isRecurring,
                  startDate,
                  endDate,
                  pickupTime,
                  pickupAddress,
                  pickupCity,
                  pickupLatitude,
                  pickupLongitude,
                  destinationAddress,
                  destinationCity,
                  destinationLatitude,
                  destinationLongitude
                )
            VALUES(
                   'kevinjames@mallcop.com',
                   'ABC123',
                   false,
                   false,
                   true,
                   false,
                   true,
                   false,
                   false,
                   true,
                   DATE('2023-11-12'),
                   DATE('2023-11-16'),
                   '8:30:00',
                   '427 Euclid Ave',
                   'Toronto',
                   43.65681,
                   -79.41160,
                   '21 Park Ln Cir',
                   'Toronto',
                   43.73466,
                   -79.37427
                  );

--all trips must have starting values associated with the coordinates.
--the coordinates will update when the trip is updated in any way
--  (other than adding a passenger depending on how that's implemented)
--this isn't an issue for normal use since the coordinates are assigned upon creation of a trip

INSERT INTO trips(
    driverEmail,
    licensePlate,
    sunday,
    monday,
    tuesday,
    wednesday,
    thursday,
    friday,
    saturday,
    isRecurring,
    startDate,
    endDate,
    pickupTime,
    pickupAddress,
    pickupCity,
    pickupLatitude,
    pickupLongitude,
    destinationAddress,
    destinationCity,
    destinationLatitude,
    destinationLongitude
)
VALUES(
          'lebron@king.net',
          'MNO012',
          false,
          false,
          true,
          false,
          true,
          false,
          false,
          true,
          DATE('2023-11-12'),
          DATE('2023-11-16'),
          '8:30:00',
          '122 Big Nickel Rd',
          'Sudbury',
          46.47402,
          -81.03401,
          '1 Bass Pro Mills Dr',
          'Vaughan',
          43.82572,
          -79.53817
      );

INSERT INTO trips(
    driverEmail,
    licensePlate,
    sunday,
    monday,
    tuesday,
    wednesday,
    thursday,
    friday,
    saturday,
    isRecurring,
    startDate,
    endDate,
    pickupTime,
    pickupAddress,
    pickupCity,
    pickupLatitude,
    pickupLongitude,
    destinationAddress,
    destinationCity,
    destinationLatitude,
    destinationLongitude
)
VALUES(
          'tswift@eras.com',
          'TS1989',
          false,
          true,
          true,
          true,
          false,
          false,
          false,
          true,
          DATE('2023-11-28'),
          DATE('2023-12-16'),
          '9:00:00',
          '55 St George St',
          'Toronto',
          43.66083,
          -79.39659,
          '900 Dufferin St',
          'Toronto',
          43.65629,
          -79.43516
      );
-- INSERT INTO trips(driverEmail, licensePlate) VALUES('djkhaled@gmail.com', 'XYZ456');
-- INSERT INTO trips(driverEmail, licensePlate) VALUES('kanye@west.org', 'JKL789');

-- INSERT INTO passengers(passengerEmail, passengerTripId) VALUES('aubreyg@yahoo.com', 2);
INSERT INTO passengers(passengerEmail, passengerTripId) VALUES('kevinjames@mallcop.com', 3);
INSERT INTO passengers(passengerEmail, passengerTripId) VALUES('kanye@west.org', 3);
INSERT INTO passengers(passengerEmail, passengerTripId) VALUES('lebron@king.net', 3);
INSERT INTO passengers(passengerEmail, passengerTripId) VALUES('djkhaled@gmail.com', 3);
INSERT INTO passengers(passengerEmail, passengerTripId) VALUES('tswift@eras.com', 1);
INSERT INTO passengers(passengerEmail, passengerTripId) VALUES('tswift@eras.com', 2);
INSERT INTO passengers(passengerEmail, passengerTripId) VALUES('kanye@west.org', 2);

INSERT INTO triprequests(requesterEmail, tripId, dateIssued, message) VALUES ('tswift@eras.com', 1, DATE('2023-12-11'), 'Something something, you belong with me, etc etc');
INSERT INTO triprequests(requesterEmail, tripId, dateIssued, message) VALUES ('djkhaled@gmail.com', 1, DATE('2023-11-30'), 'DJ KHALED!!!!!');
INSERT INTO triprequests(requesterEmail, tripId, dateIssued, message) VALUES ('curry@chef.com', 3, DATE('2023-11-23'), '4 RINGS!!!!');
INSERT INTO triprequests(requesterEmail, tripId, dateIssued, message) VALUES ('aubreyg@yahoo.com', 2, DATE('2023-11-20'), '21 can you do sumthin for me :3 UwU');
INSERT INTO triprequests(requesterEmail, tripId, dateIssued, message) VALUES ('kevinjames@mallcop.com', 2, DATE('2023-11-24'), 'PLEASE LET ME IN');