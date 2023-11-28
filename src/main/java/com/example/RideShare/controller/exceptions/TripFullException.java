package com.example.RideShare.controller.exceptions;

public class TripFullException extends RuntimeException{

    public TripFullException(long tripId){

        super(String.format("Trip (ID: %l) doesn't have any space left for a passanger.", tripId));
    }
}
