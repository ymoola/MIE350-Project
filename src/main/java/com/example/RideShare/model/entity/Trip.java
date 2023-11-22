package com.example.RideShare.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import javax.persistence.*;
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


    //think of a way to best store addresses for geocoding (goes for both this entity and the user entities)
//    @NotEmpty
//    private String pickupAddress;
//
//    @NotEmpty
//    private String destinationAddress;
}
