package com.example.RideShare.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleDto {
    private String licensePlate;
    private String make;
    private String model;
    private String type;
    private int passengerSeats;
    private String color;
    private String ownerEmail;
}
