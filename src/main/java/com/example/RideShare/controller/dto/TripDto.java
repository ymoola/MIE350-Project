package com.example.RideShare.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.annotation.Nullable;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

//copy paste field for testing
//highlight everything, press command / to uncomment, copy, then press command / again to recomment

//{
//    "licensePlate": "ABC123",
//    "sunday": false,
//    "monday": true,
//    "tuesday": true,
//    "wednesday": true,
//    "thursday": true,
//    "friday": true,
//    "saturday": false,
//    "startDate": "2023-11-12",
//    "endDate": "2023-11-16",
//    "pickupTime": "8:30:00",
//    "pickupAddress": "427 Euclid Ave",
//    "pickupCity": "Toronto",
//    "pickupAreaCode": null,
//    "destinationAddress": "21 Park Ln Cir",
//    "destinationCity": "Toronto",
//    "destinationAreaCode": null,
//    "recurring": true
//}

@Getter
@Setter
public class TripDto {
    private String licensePlate;

    private boolean sunday;

    private boolean monday;

    private boolean tuesday;

    private boolean wednesday;

    private boolean thursday;

    private boolean friday;

    private boolean saturday;

    private boolean isRecurring;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;

    private String pickupTime;

    private String pickupAddress;
    private String pickupCity;
    private String pickupAreaCode;

    private String destinationAddress;
    private String destinationCity;
    private String destinationAreaCode;
}
