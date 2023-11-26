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
INSERT INTO vehicles(licensePlate, passengerSeats, ownerEmail) VALUES('MNO012', 5, 'lebron@king.net');
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
                   0,
                   0,
                   '21 Park Ln Cir',
                   'Toronto',
                   0,
                   0
                  );

-- INSERT INTO trips(driverEmail, licensePlate) VALUES('djkhaled@gmail.com', 'XYZ456');
-- INSERT INTO trips(driverEmail, licensePlate) VALUES('kanye@west.org', 'JKL789');
-- INSERT INTO trips(driverEmail, licensePlate) VALUES('lebron@king.net', 'MNO012');
-- INSERT INTO trips(driverEmail, licensePlate) VALUES('aubreyg@yahoo.com', 'PQR345');
--
-- INSERT INTO passengers(passengerEmail, passengerTripId) VALUES('aubreyg@yahoo.com', 4);
-- INSERT INTO passengers(passengerEmail, passengerTripId) VALUES('curry@chef.com', 3);
-- INSERT INTO passengers(passengerEmail, passengerTripId) VALUES('tswift@eras.com', 2);
-- INSERT INTO passengers(passengerEmail, passengerTripId) VALUES('kanye@west.org', 2);
-- INSERT INTO passengers(passengerEmail, passengerTripId) VALUES('djkhaled@gmail.com', 1);