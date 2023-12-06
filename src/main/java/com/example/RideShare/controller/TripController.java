package com.example.RideShare.controller;

import com.example.RideShare.controller.dto.TripDto;
import com.example.RideShare.controller.exceptions.*;
import com.example.RideShare.model.entity.Trip;
import com.example.RideShare.model.entity.User;
import com.example.RideShare.model.entity.Vehicle;
import com.example.RideShare.model.repository.TripRepository;
import com.example.RideShare.model.repository.UserRepository;
import com.example.RideShare.model.repository.VehicleRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.example.RideShare.geocoding.Geocoder.GeocodeSync;

@RestController
@RequestMapping("/trips")
public class TripController {

    //address variables for API
    private final String region = "ON";
    private final String country = "Canada";

    private JSONObject coords;

    @Autowired
    private final TripRepository repository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final VehicleRepository vehicleRepository;

    public TripController(TripRepository repository,
                          UserRepository userRepository,
                          VehicleRepository vehicleRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping
    public List<Trip> getAllTrips() {
        return repository.findAll();
    }

    @PostMapping
    public Trip createTrip(Authentication authentication, @RequestBody TripDto tripDto) {
        //set driver
        Trip newTrip = new Trip();
        User driver = userRepository.findById(authentication.getName()).orElseThrow(
                () -> new UserNotFoundException(authentication.getName()));
        newTrip.setDriver(driver);

        //set Vehicle
        Vehicle vehicle = vehicleRepository.findById(tripDto.getLicensePlate()).orElseThrow(
                () -> new VehicleNotFoundException(tripDto.getLicensePlate()));

        //check if vehicle belongs to user
        if (!vehicle.getOwner().getEmail().equals(authentication.getName()))
            throw new VehicleOwnerIncorrectException(tripDto.getLicensePlate(), authentication.getName());
        newTrip.setVehicle(vehicle);

        //set the days of weeks the trip is going on for
        newTrip.setSunday(tripDto.isSunday());
        newTrip.setMonday(tripDto.isMonday());
        newTrip.setTuesday(tripDto.isTuesday());
        newTrip.setWednesday(tripDto.isWednesday());
        newTrip.setThursday(tripDto.isThursday());
        newTrip.setFriday(tripDto.isFriday());
        newTrip.setSaturday(tripDto.isSaturday());
        
        //set start date
        if (tripDto.getStartDate() != null)
            newTrip.setStartDate(tripDto.getStartDate());
        
        //set is Recurring
        newTrip.setRecurring(tripDto.isRecurring());

        //set end date
        if (tripDto.getEndDate() != null)
            newTrip.setEndDate(tripDto.getEndDate());

        //set pickup time
        newTrip.setPickupTime(tripDto.getPickupTime());

        //set pickup address fields
        newTrip.setPickupAddress(tripDto.getPickupAddress());
        newTrip.setPickupCity(tripDto.getPickupCity());
        newTrip.setPickupAreaCode(tripDto.getPickupAreaCode());
        if(tripDto.getPickupAreaCode() != null){
            //spring made me put this try catch in, it should never catch as long as
            //there aren't any abnormalities in the address fields
            try {
                coords = GeocodeSync(tripDto.getPickupAddress()
                        + " " + tripDto.getPickupCity() + " " + region + " " + country
                        + " " + tripDto.getPickupAreaCode());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            try {
                coords = GeocodeSync(tripDto.getPickupAddress()
                        + " " + tripDto.getPickupCity() + " " + region + " " + country);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        newTrip.setPickupLatitude(coords.getDouble("lat"));
        newTrip.setPickupLongitude(coords.getDouble("lng"));

        //set destination address fields
        newTrip.setDestinationAddress(tripDto.getDestinationAddress());
        newTrip.setDestinationCity(tripDto.getDestinationCity());
        newTrip.setDestinationAreaCode(tripDto.getDestinationAreaCode());
        if(tripDto.getDestinationAreaCode() != null){
            //spring made me put this try catch in, it should never catch as long as
            //there aren't any abnormalities in the address fields
            try {
                coords = GeocodeSync(tripDto.getDestinationAddress()
                        + " " + tripDto.getDestinationCity() + " " + region + " " + country
                        + " " + tripDto.getDestinationAreaCode());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            try {
                coords = GeocodeSync(tripDto.getDestinationAddress()
                        + " " + tripDto.getDestinationCity() + " " + region + " " + country);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        newTrip.setDestinationLatitude(coords.getDouble("lat"));
        newTrip.setDestinationLongitude(coords.getDouble("lng"));

        return repository.save(newTrip);
    }

    @GetMapping("/{tripId}")
    public Trip getTripById(@PathVariable Long tripId) {
        return repository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException(tripId)); // Custom exception
    }

    // Update Trip
    @PutMapping("/{tripId}")
    public Trip updateTrip(Authentication authentication, @RequestBody TripDto updatedTripDto, @PathVariable Long tripId) {
        return repository.findById(tripId)
                .map(trip -> {
                    Vehicle vehicle = vehicleRepository.findById(updatedTripDto.getLicensePlate()).orElseThrow(
                            () -> new VehicleNotFoundException(updatedTripDto.getLicensePlate()));

                    if (!vehicle.getOwner().getEmail().equals(authentication.getName()))
                        throw new VehicleOwnerIncorrectException(updatedTripDto.getLicensePlate(), authentication.getName());
                    trip.setVehicle(vehicle);
                    //set the days of weeks the trip is going on for
                    trip.setSunday(updatedTripDto.isSunday());
                    trip.setMonday(updatedTripDto.isMonday());
                    trip.setTuesday(updatedTripDto.isTuesday());
                    trip.setWednesday(updatedTripDto.isWednesday());
                    trip.setThursday(updatedTripDto.isThursday());
                    trip.setFriday(updatedTripDto.isFriday());
                    trip.setSaturday(updatedTripDto.isSaturday());

                    //set start date
                    if (updatedTripDto.getStartDate() != null)
                        trip.setStartDate(updatedTripDto.getStartDate());

                    //set is Recurring
                    trip.setRecurring(updatedTripDto.isRecurring());

                    //set end date
                    if (updatedTripDto.getEndDate() != null)
                        trip.setEndDate(updatedTripDto.getEndDate());

                    //set pickup time
                    trip.setPickupTime(updatedTripDto.getPickupTime());

                    //set pickup address fields
                    trip.setPickupAddress(updatedTripDto.getPickupAddress());
                    trip.setPickupCity(updatedTripDto.getPickupCity());
                    trip.setPickupAreaCode(updatedTripDto.getPickupAreaCode());
                    if(updatedTripDto.getPickupAreaCode() != null){
                        //spring made me put this try catch in, it should never catch as long as
                        //there aren't any abnormalities in the address fields
                        try {
                            coords = GeocodeSync(updatedTripDto.getPickupAddress()
                            + " " + updatedTripDto.getPickupCity() + " " + region + " " + country
                            + " " + updatedTripDto.getPickupAreaCode());
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        try {
                            coords = GeocodeSync(updatedTripDto.getPickupAddress()
                                    + " " + updatedTripDto.getPickupCity() + " " + region + " " + country);
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    trip.setPickupLatitude(coords.getDouble("lat"));
                    trip.setPickupLongitude(coords.getDouble("lng"));

                    //set destination address fields
                    trip.setDestinationAddress(updatedTripDto.getDestinationAddress());
                    trip.setDestinationCity(updatedTripDto.getDestinationCity());
                    trip.setDestinationAreaCode(updatedTripDto.getDestinationAreaCode());
                    if(updatedTripDto.getDestinationAreaCode() != null){
                        try {
                            coords = GeocodeSync(updatedTripDto.getDestinationAddress()
                                    + " " + updatedTripDto.getDestinationCity() + " " + region + " " + country
                                    + " " + updatedTripDto.getDestinationAreaCode());
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        try {
                            coords = GeocodeSync(updatedTripDto.getDestinationAddress()
                                    + " " + updatedTripDto.getDestinationCity() + " " + region + " " + country);
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    trip.setDestinationLatitude(coords.getDouble("lat"));
                    trip.setDestinationLongitude(coords.getDouble("lng"));

                    return repository.save(trip);
                })
                .orElseThrow(() -> new TripNotFoundException(tripId));
    }

    // Delete Trip
    @DeleteMapping("/{tripId}")
    public void deleteTrip(Authentication authentication, @PathVariable Long tripId) {
        Trip tripToDelete = repository.findById(tripId)
                .orElseThrow(()-> new TripNotFoundException(tripId));

        if (!tripToDelete.getDriver().getEmail().equals(authentication.getName()))
            throw new TripWriteUnauthorizedException(tripId);

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

    @GetMapping("/getPassengerTrips/{email}")
    List<Trip> getPassengerTrips(@PathVariable String email) {
        return repository.getByPassenger(email);
    }
}
