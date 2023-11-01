package com.example.RideShare.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//do we need this @ResponseStatus? Should we put it in all exceptions?
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TripNotFoundException extends RuntimeException {
    public TripNotFoundException(Long tripId) {
        super("Trip not found with ID: " + tripId);
    }
}
