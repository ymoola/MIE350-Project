package com.example.RideShare.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripDto {
    private Long tripId;
    private String driverEmail;
    private String licensePlate;
}
