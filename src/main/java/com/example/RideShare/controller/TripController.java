package com.example.RideShare.controller;

import com.example.RideShare.controller.dto.TripDto;
import com.example.RideShare.controller.exceptions.TripNotFoundException;
import com.example.RideShare.controller.exceptions.UserNotFoundException;
import com.example.RideShare.controller.exceptions.VehicleNotFoundException;
import com.example.RideShare.model.entity.Trip;
import com.example.RideShare.model.entity.User;
import com.example.RideShare.model.entity.Vehicle;
import com.example.RideShare.model.repository.TripRepository;
import com.example.RideShare.model.repository.UserRepository;
import com.example.RideShare.model.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private final TripRepository repository;

    @Autowired
    private UserRepository userRepository;

    public TripController(TripRepository repository) {this.repository = repository;}

    @Autowired
    private VehicleRepository vehicleRepository;

    @GetMapping
    public List<Trip> getAllTrips() {
        return repository.findAll();
    }

    @PostMapping
    public Trip createTrip(@RequestBody TripDto tripDto) {
        Trip newTrip = new Trip();
        newTrip.setTripId(tripDto.getTripId());
        User driver = userRepository.findById(tripDto.getDriverEmail()).orElseThrow(
                () -> new UserNotFoundException(tripDto.getDriverEmail()));
        newTrip.setUser(driver);
        Vehicle vehicle = vehicleRepository.findById(tripDto.getLicensePlate()).orElseThrow(
                () -> new VehicleNotFoundException(tripDto.getLicensePlate()));
        newTrip.setVehicle(vehicle);
        return repository.save(newTrip);
    }

    @GetMapping("/{tripId}")
    public Trip getTripById(@PathVariable Long tripId) {
        return repository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException(tripId)); // Custom exception
    }

    // Update Trip
    @PutMapping("/{tripId}")
    public Trip updateTrip(@RequestBody TripDto updatedTripDto, @PathVariable Long tripId) {
        return repository.findById(tripId)
                .map(trip -> {
                    User driver = userRepository.findById(updatedTripDto.getDriverEmail()).orElseThrow(
                            () -> new UserNotFoundException(updatedTripDto.getDriverEmail()));
                    trip.setUser(driver);
                    Vehicle vehicle = vehicleRepository.findById(updatedTripDto.getLicensePlate()).orElseThrow(
                            () -> new VehicleNotFoundException(updatedTripDto.getLicensePlate()));
                    trip.setVehicle(vehicle);
                    return repository.save(trip);
                })
                .orElseThrow(() -> new TripNotFoundException(tripId)); // Custom exception
    }

    // Delete Trip
    @DeleteMapping("/{tripId}")
    public void deleteTrip(@PathVariable Long tripId) {
        repository.deleteById(tripId);
    }

    @GetMapping("/searchByName/{searchstring}")
    List<Trip> searchByDriver(@PathVariable("searchstring") String searchString){
        return repository.searchByDriver(searchString);
    }

    @GetMapping("/getDriverTrips/{email}")
    List<Trip> getDriverTrips(@PathVariable("email") String email){
        return repository.getDriverTrips(email);
    }
}
