package com.example.RideShare.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TripRequestDecisionUnauthorizedException extends RuntimeException {
    public TripRequestDecisionUnauthorizedException(long tripId, String email) {
        super(String.format("Decision on state for Trip Request (tripId: %d, requesterEmail: %s) is unauthorized by the requester. ", tripId, email));
    }
}
