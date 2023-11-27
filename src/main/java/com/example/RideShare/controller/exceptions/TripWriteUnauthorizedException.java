package com.example.RideShare.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TripWriteUnauthorizedException extends RuntimeException {
    public TripWriteUnauthorizedException(Long tripId) {
        super(String.format("Unauthorized write to trip with id: %s", String.valueOf(tripId)));
    }
}
