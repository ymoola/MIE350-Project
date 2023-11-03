package com.example.RideShare.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripDto {
    // for now we are not including date/time, this will be added later
    // the details of this is given by the user

    private Long tripId;
    private String driverEmail;
    private String licensePlate;
}
