package com.example.RideShare.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PassengerNotFoundException extends RuntimeException {
    public PassengerNotFoundException(long tripId, String passengerEmail) {
        super(String.format( "Passenger in trip, %d, with email, %s, does not exist.", tripId, passengerEmail));
    }
}
