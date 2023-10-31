INSERT INTO users(email, password, firstName, lastName) VALUES('kevinjames@mallcop.com', 'segway', 'Kevin', 'James');
INSERT INTO users(email, password, firstName, lastName) VALUES('djkhaled@gmail.com', 'anothaOne', 'DJ', 'Khaled');
INSERT INTO users(email, password, firstName, lastName) VALUES('kanye@west.org', 'buyDaEarf123', 'Kanye', 'West');
INSERT INTO users(email, password, firstName, lastName) VALUES('lebron@king.net', 'Strive4Gr8ness', 'LeBron', 'James');
INSERT INTO users(email, password, firstName, lastName) VALUES('aubreyg@yahoo.com', 'money4fun', 'Aubrey', 'Graham');

INSERT INTO vehicles(licensePlate, passengerSeats, ownerEmail) VALUES('ABC123', 5, 'kevinjames@mallcop.com');
INSERT INTO vehicles(licensePlate, passengerSeats, ownerEmail) VALUES('XYZ456', 7, 'djkhaled@gmail.com');
INSERT INTO vehicles(licensePlate, passengerSeats, ownerEmail) VALUES('JKL789', 3, 'kanye@west.org');
INSERT INTO vehicles(licensePlate, passengerSeats, ownerEmail) VALUES('MNO012', 5, 'lebron@king.net');
INSERT INTO vehicles(licensePlate, passengerSeats, ownerEmail) VALUES('PQR345', 3, 'aubreyg@yahoo.com');

INSERT INTO trips(tripId, driverEmail, licensePlate) VALUES(1, 'kevinjames@mallcop.com', 'ABC123');
INSERT INTO trips(tripId, driverEmail, licensePlate) VALUES(2, 'djkhaled@gmail.com', 'XYZ456');
INSERT INTO trips(tripId, driverEmail, licensePlate) VALUES(3, 'kanye@west.org', 'JKL789');
INSERT INTO trips(tripId, driverEmail, licensePlate) VALUES(4, 'lebron@king.net', 'MNO012');
INSERT INTO trips(tripId, driverEmail, licensePlate) VALUES(5, 'aubreyg@yahoo.com', 'PQR345');
