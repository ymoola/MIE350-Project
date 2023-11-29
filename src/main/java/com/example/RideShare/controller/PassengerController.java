package com.example.RideShare.controller;

import com.example.RideShare.controller.exceptions.PassengerNotFoundException;
import com.example.RideShare.controller.exceptions.PassengerWriteException;
import com.example.RideShare.controller.exceptions.TripNotFoundException;
import com.example.RideShare.model.entity.Passenger;
import com.example.RideShare.model.entity.Trip;
import com.example.RideShare.model.keys.PassengerKey;
import com.example.RideShare.model.repository.PassengerRepository;
import com.example.RideShare.model.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

    @Autowired
    private final PassengerRepository repository;

    @Autowired
    private final TripRepository tripRepository;

    public PassengerController(PassengerRepository repository, TripRepository tripRepository) {
        this.repository = repository;
        this.tripRepository = tripRepository;
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
    Passenger getByKey(@PathVariable long tripId, @PathVariable String passengerEmail) {
        return repository.findById(new PassengerKey(tripId, passengerEmail))
                .orElseThrow(() -> new PassengerNotFoundException(tripId, passengerEmail));
    }

    @DeleteMapping("/{tripId}/{passengerEmail}")
    void deletePassenger(Authentication authentication, @PathVariable("tripId") long tripId, @PathVariable("passengerEmail") String passengerEmail){
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(()-> new TripNotFoundException(tripId));

        if (!trip.getDriver().getEmail().equals(authentication.getName()) && !authentication.getName().equals(passengerEmail))
            throw new PassengerWriteException(tripId, passengerEmail);

        repository.deleteById(new PassengerKey(tripId, passengerEmail));
    }


}
