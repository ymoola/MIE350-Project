package com.example.RideShare.controller;

import com.example.RideShare.controller.exceptions.TripNotFoundException;
import com.example.RideShare.model.entity.Trip;
import com.example.RideShare.model.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripRepository tripRepository;

    @GetMapping
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    @PostMapping
    public Trip createTrip(@RequestBody Trip trip) {
        return tripRepository.save(trip);
    }

    @GetMapping("/{tripId}")
    public Trip getTripById(@PathVariable Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException(tripId)); // Custom exception
    }

    // Update Trip
    @PutMapping("/{tripId}")
    public Trip updateTrip(@RequestBody Trip updatedTrip, @PathVariable Long tripId) {
        return tripRepository.findById(tripId)
                .map(trip -> {
                    trip.setUser(updatedTrip.getUser());
                    trip.setVehicle(updatedTrip.getVehicle());
                    // Set other fields if necessary
                    return tripRepository.save(trip);
                })
                .orElseThrow(() -> new TripNotFoundException(tripId)); // Custom exception
    }

    // Delete Trip
    @DeleteMapping("/{tripId}")
    public void deleteTrip(@PathVariable Long tripId) {
        tripRepository.deleteById(tripId);
    }
}
