package com.example.RideShare.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PassangerAlreadyRegisteredException extends RuntimeException {
    public PassangerAlreadyRegisteredException (long tripId, String email){
        super(String.format("User with email %s already registered for trip (ID: %d).", email, tripId));

    }
}
