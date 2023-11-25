package com.example.RideShare.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    @ManyToOne
    @JoinColumn(name = "driverEmail")
    private User driver;

    @OneToOne
    @JoinColumn(name = "licensePlate")
    private Vehicle vehicle;

    @OneToMany(mappedBy = "trip")
    @Nullable
    private List<Passenger> passengers = new ArrayList<>();

    private boolean sunday;

    private boolean monday;

    private boolean tuesday;

    private boolean wednesday;

    private boolean thursday;

    private boolean friday;

    private boolean saturday;

    private boolean isRecurring;

    @Temporal(TemporalType.DATE)
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Nullable
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;

    private String pickupTime;


    //The area code is an optional field since the API doesn't need it
    //right now I'm storing the coordinates as individual type-double variables for latitude and longitude
    //for some reason Spring didn't like it when I tried to have a private JSONObject
    //The API on the front end might need the coordinates in a different data type (float, double, etc.)

    //number and street name only
    @NotEmpty
    private String pickupAddress;

    @NotEmpty
    private String pickupCity;

    private String pickupAreaCode;

    private double pickupLatitude;

    private double pickupLongitude;

    //number and street name only
    @NotEmpty
    private String destinationAddress;

    @NotEmpty
    private String destinationCity;

    private String destinationAreaCode;

    private double destinationLatitude;

    private double destinationLongitude;
}
