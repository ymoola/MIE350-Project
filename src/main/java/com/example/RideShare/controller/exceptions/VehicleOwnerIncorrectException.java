package com.example.RideShare.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class VehicleOwnerIncorrectException extends RuntimeException {
    public VehicleOwnerIncorrectException(String owner, String licensePlate) {
        super(String.format("Vehicle with license plate, %s, does not have its owner as %s", licensePlate, owner));
    }
}
