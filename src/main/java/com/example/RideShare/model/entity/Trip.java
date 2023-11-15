package com.example.RideShare.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "trips")
public class Trip {

    //for now we are not including date/time, this will be added later

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //this generated value thing makes it weird, I think we shouldn't have it and instead we can have a counter somewhere in order to id trips
    //we can also do this for trip requests and anything else that we need internally generated ids for
    private Long tripId;

    @ManyToOne
    @JoinColumn(name = "driverEmail")
    private User driver;

    // Assuming a relationship with Vehicle
    @OneToOne
    @JoinColumn(name = "licensePlate")
    private Vehicle vehicle;

    // Additional fields like startLocation, endLocation, dateTime, etc.
}
