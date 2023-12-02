package com.example.RideShare.model.entity;

import com.example.RideShare.model.keys.PassengerKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
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
    @JsonIgnoreProperties({"isPassengerInstances", "password"})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @MapsId("passengerTripId")
    @JoinColumn(name = "passengerTripId")
    @JsonIgnoreProperties({"passengers"})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Trip trip;
}
