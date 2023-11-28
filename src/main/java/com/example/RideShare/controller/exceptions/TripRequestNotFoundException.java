package com.example.RideShare.controller.exceptions;

// TODO: add the response status here
public class TripRequestNotFoundException extends RuntimeException{

    public TripRequestNotFoundException(long tripId, String email){
        super(String.format("There exists no requests for trip %l by user %s", tripId, email));
    }
}
