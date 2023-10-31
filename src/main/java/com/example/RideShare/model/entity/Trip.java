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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    @ManyToOne
    @JoinColumn(name = "driverEmail")
    private User user;

    // Assuming a relationship with Vehicle
    @OneToOne
    @JoinColumn(name = "licensePlate")
    private Vehicle vehicle;

    // Additional fields like startLocation, endLocation, dateTime, etc.
}
