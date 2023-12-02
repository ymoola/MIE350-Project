package com.example.RideShare.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TripFullException extends RuntimeException {
    public TripFullException(long tripId){
        super(String.format("Trip (ID: %d) has already reached passenger capacity.", tripId));
    }
}
