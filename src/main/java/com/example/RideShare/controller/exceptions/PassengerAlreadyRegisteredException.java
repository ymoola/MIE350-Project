package com.example.RideShare.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PassengerAlreadyRegisteredException extends RuntimeException {
    public PassengerAlreadyRegisteredException(long tripId, String email){
        super(String.format("User with email %s already registered for trip (ID: %d).", email, tripId));

    }
}
