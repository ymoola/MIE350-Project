package com.example.RideShare.controller;

import com.example.RideShare.controller.dto.TripDto;
import com.example.RideShare.controller.exceptions.TripNotFoundException;
import com.example.RideShare.controller.exceptions.TripWriteUnauthorizedException;
import com.example.RideShare.controller.exceptions.UserNotFoundException;
import com.example.RideShare.controller.exceptions.VehicleNotFoundException;
import com.example.RideShare.model.entity.Trip;
import com.example.RideShare.model.entity.User;
import com.example.RideShare.model.entity.Vehicle;
import com.example.RideShare.model.repository.PassengerRepository;
import com.example.RideShare.model.repository.TripRepository;
import com.example.RideShare.model.repository.UserRepository;
import com.example.RideShare.model.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private final TripRepository repository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final VehicleRepository vehicleRepository;

    @Autowired
    private final PassengerRepository passengerRepository;

    public TripController(TripRepository repository,
                          UserRepository userRepository,
                          VehicleRepository vehicleRepository,
                          PassengerRepository passengerRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.passengerRepository = passengerRepository;
    }

    @GetMapping
    public List<Trip> getAllTrips() {
        return repository.findAll();
    }

    @PostMapping
    @PostAuthorize("authentication.principal == returnObject.vehicle.owner.email")
    public Trip createTrip(Authentication authentication, @RequestBody TripDto tripDto) {
        Trip newTrip = new Trip();
        User driver = userRepository.findById(authentication.getName()).orElseThrow(
                () -> new UserNotFoundException(authentication.getName()));
        newTrip.setDriver(driver);
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
    @PostAuthorize("(returnObject.driver.email == authentication.principal) &&" +
            "(returnObject.vehicle.owner.email == authentication.principal)")
    public Trip updateTrip(@RequestBody TripDto updatedTripDto, @PathVariable Long tripId) {
        return repository.findById(tripId)
                .map(trip -> {
                    Vehicle vehicle = vehicleRepository.findById(updatedTripDto.getLicensePlate()).orElseThrow(
                            () -> new VehicleNotFoundException(updatedTripDto.getLicensePlate()));
                    trip.setVehicle(vehicle);
                    return repository.save(trip);
                })
                .orElseThrow(() -> new TripNotFoundException(tripId)); // Custom exception
    }

    // Delete Trip
    @DeleteMapping("/{tripId}")
    @Transactional
    public void deleteTrip(Authentication authentication, @PathVariable Long tripId) {
        Trip tripToDelete = repository.findById(tripId)
                .orElseThrow(()-> new TripNotFoundException(tripId));

        if (!tripToDelete.getDriver().getEmail().equals(authentication.getName()))
            throw new TripWriteUnauthorizedException(tripId);

        //delete every passenger instance that had this trip as a foreign key
        passengerRepository.deleteAllByTripId(tripId);

        repository.deleteById(tripId);
    }

    @GetMapping("/searchByDriverName/{searchstring}")
    List<Trip> searchByDriver(@PathVariable("searchstring") String searchString){
        return repository.searchByDriverName(searchString);
    }


    @GetMapping("/getDriverTrips/{email}")
    List<Trip> getDriverTrips(@PathVariable("email") String email){
        return repository.getByDriverEmail(email);
    }
}
