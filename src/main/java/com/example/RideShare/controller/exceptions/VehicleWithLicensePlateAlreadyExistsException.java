package com.example.RideShare.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class VehicleWithLicensePlateAlreadyExistsException extends RuntimeException {
    public VehicleWithLicensePlateAlreadyExistsException(String licensePlate) {
        super(String.format("Vehicle with license plate, %s, already exists.", licensePlate));
    }
}
