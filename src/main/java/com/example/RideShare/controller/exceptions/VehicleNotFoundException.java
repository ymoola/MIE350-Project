package com.example.RideShare.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.GONE)
public class VehicleNotFoundException extends RuntimeException{
    public VehicleNotFoundException(String licensePlate) {
        super("Vehicle with license plate " + licensePlate + " not found!");
    }
}
