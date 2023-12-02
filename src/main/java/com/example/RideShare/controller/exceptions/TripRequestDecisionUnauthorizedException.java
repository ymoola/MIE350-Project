package com.example.RideShare.controller.exceptions;

public class TripRequestDecisionUnauthorizedException extends RuntimeException {
    public TripRequestDecisionUnauthorizedException(long tripId, String email) {
        super(String.format("Decision on state for Trip Request (tripId: %d, requesterEmail: %s) is unauthorized by the requester. ", tripId, email));
    }
}
