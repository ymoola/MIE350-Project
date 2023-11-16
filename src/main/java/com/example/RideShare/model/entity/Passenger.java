package com.example.RideShare.model.entity;

import com.example.RideShare.model.keys.PassengerKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

// to store instances where this user is a passenger for a trip.

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "passengers")
public class Passenger {

    @EmbeddedId
    PassengerKey passengerKey;

    @ManyToOne
    @MapsId("passengerEmail")
    @JoinColumn(name = "passengerEmail")
    @JsonIgnoreProperties({"passengers"})
    private User user;

    @ManyToOne
    @MapsId("passengerTripId")
    @JoinColumn(name = "passengerTripId")
    @JsonIgnoreProperties({"passengers"})
    private Trip trip;

}
