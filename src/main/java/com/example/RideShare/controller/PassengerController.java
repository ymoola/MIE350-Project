package com.example.RideShare.controller;

import com.example.RideShare.controller.exceptions.PassengerNotFoundException;
import com.example.RideShare.model.entity.Passenger;
import com.example.RideShare.model.keys.PassengerKey;
import com.example.RideShare.model.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

    @Autowired
    private final PassengerRepository repository;

    public PassengerController(PassengerRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    List<Passenger> getAllPassengers() {
        return repository.findAll();
    }

    @GetMapping("/byTrip/{tripId}")
    List<Passenger> getPassengersInTrip(@PathVariable long tripId) {
        return repository.findAllByTripId(tripId);
    }

    @GetMapping("/byEmail/{email}")
    List<Passenger> getPassengersByEmail(@PathVariable String email) {
        return repository.findAllByPassengerEmail(email);
    }

    @GetMapping("/{tripId}/{passengerEmail}")
    Passenger getByKey(@PathVariable("tripId") long tripId, @PathVariable("passengerEmail") String passengerEmail) {
        return repository.findById(new PassengerKey(tripId, passengerEmail))
                .orElseThrow(() -> new PassengerNotFoundException(tripId, passengerEmail));
    }

    //TODO: passanger tries to delete a trip they are signed up for

}
