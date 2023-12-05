package com.example.RideShare.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class PassengerWriteException extends RuntimeException {
    public PassengerWriteException(long tripId, String email) {
        super(String.format("Unauthorized to make changes to passenger(tripId: %d, passengerEmail: %s).", tripId, email));
    }
}
