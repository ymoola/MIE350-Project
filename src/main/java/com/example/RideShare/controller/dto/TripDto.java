package com.example.RideShare.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripDto {
    //for now we are not including date/time, this will be added later
    private String licensePlate;
}
