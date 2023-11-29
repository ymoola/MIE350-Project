package com.example.RideShare.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TripRequestNotFoundException extends RuntimeException{
    public TripRequestNotFoundException(long tripId, String email){
        super(String.format("There exists no requests for trip %d by user %s", tripId, email));
    }
}
