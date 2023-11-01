package com.example.RideShare.controller.exceptions;

public class VehicleNotFoundException extends RuntimeException{
    public VehicleNotFoundException(String licensePlate) {
        super("Vehicle with license plate " + licensePlate + " not found!");
    }
}
