package com.example.RideShare.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @NotEmpty
    private String licensePlate;

    @Nullable
    private String make;

    @Nullable
    private String model;

    //type of car (SUV, etc.)
    @Nullable
    private String type;

    //Not including driver
    @NotNull
    private int passengerSeats;

    @Nullable
    private String color;

    @ManyToOne
    @JoinColumn(name = "ownerEmail")
    @JsonIgnoreProperties({"password", "isPassengerInstances"})
    private User owner;
}