package com.example.RideShare.controller;

import com.example.RideShare.controller.dto.TripRequestDto;
import com.example.RideShare.controller.exceptions.*;
import com.example.RideShare.model.entity.Passenger;
import com.example.RideShare.model.entity.Trip;
import com.example.RideShare.model.entity.TripRequest;
import com.example.RideShare.model.entity.User;
import com.example.RideShare.model.keys.PassengerKey;
import com.example.RideShare.model.keys.TripRequestKey;
import com.example.RideShare.model.repository.PassengerRepository;
import com.example.RideShare.model.repository.TripRepository;
import com.example.RideShare.model.repository.TripRequestRepository;
import com.example.RideShare.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/triprequests")
public class TripRequestController {

    @Autowired
    private final TripRequestRepository repository;

    @Autowired
    private final TripRepository tripRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PassengerRepository passengerRepository;

    public TripRequestController(TripRequestRepository repository, TripRepository tripRepository, UserRepository userRepository, PassengerRepository passengerRepository){
        this.repository = repository;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.passengerRepository = passengerRepository;
    }

    @GetMapping
    List<TripRequest> getAllRequests() {
        return repository.findAll();
    }

    //search by trip
    @GetMapping("/byTrip/{tripId}")
    List<TripRequest> getByTrip(@PathVariable long tripId) {
        return repository.getByTrip(tripId);
    }

    @GetMapping("/byRequester/{email}")
    List<TripRequest> getByRequester(@PathVariable String email) {
        return repository.getByUser(email);
    }

    //by driver
    @GetMapping("/byDriver/{email}")
    List<TripRequest> getByTripDriver(@PathVariable String email) {return repository.getByDriver(email); }

    @PostMapping
    TripRequest makeTripRequest(Authentication authentication, @RequestBody TripRequestDto tripRequestDto) {
        String requesterEmail = authentication.getName();
        User user = userRepository.findById(requesterEmail)
                .orElseThrow(() -> new UserNotFoundException(requesterEmail));

        Trip trip = tripRepository.findById(tripRequestDto.getTripId()).orElseThrow(
                () -> new TripNotFoundException(tripRequestDto.getTripId()));
        int capacity = trip.getVehicle().getPassengerSeats();

        PassengerKey passangerKey = new PassengerKey(tripRequestDto.getTripId(), requesterEmail);
        if (passengerRepository.existsById(passangerKey)){
          throw new PassangerAlreadyRegisteredException(tripRequestDto.getTripId(), requesterEmail);
        }
        if (trip.getPassengers() != null && trip.getPassengers().size() == capacity) {
            throw new TripFullException(trip.getTripId());
        }

        TripRequestKey key = new TripRequestKey(tripRequestDto.getTripId(), requesterEmail);
        TripRequest newRequest = new TripRequest();
        newRequest.setTripRequestKey(key);
        newRequest.setTrip(trip);
        newRequest.setUser(user);
        newRequest.setMessage(tripRequestDto.getMessage());
        newRequest.setDateIssued(new Date()); // constructor sets it to current time

        return repository.save(newRequest);
    }

    @PutMapping("/{tripId}/{email}")
    @PreAuthorize("authentication.principal == #email")
    TripRequest updateRequest(@PathVariable("tripId") long tripId, @PathVariable("email") String email, @RequestBody TripRequestDto tripRequestDto){
        TripRequestKey key = new TripRequestKey(tripId, email);
        TripRequest tripRequest = repository.findById(key)
                .orElseThrow(() -> new TripRequestNotFoundException(tripId, email));
        tripRequest.setMessage(tripRequestDto.getMessage());
        tripRequest.setDateIssued(new Date());
        return repository.save(tripRequest);
    }


    @DeleteMapping("/decline/{tripId}/{email}")
    void declineRequest(Authentication authentication, @PathVariable long tripId, @PathVariable String email){
        Trip trip = tripRepository.findById(tripId)
                        .orElseThrow(()->new TripNotFoundException(tripId));

        if (!trip.getDriver().getEmail().equals(authentication.getName()) && !email.equals(authentication.getName()))
            throw new TripRequestDecisionUnauthorizedException(tripId, email);

        repository.deleteById(new TripRequestKey(tripId,email));
    }

    @DeleteMapping("accept/{tripId}/{userEmail}")
    void acceptRequest(Authentication authentication, @PathVariable long tripId, @PathVariable String email){
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException(tripId));

        if (!trip.getDriver().getEmail().equals(authentication.getName()))
            throw new TripRequestDecisionUnauthorizedException(tripId, email);

        int capacity = trip.getVehicle().getPassengerSeats();
        //throw exception if already full
        if (trip.getPassengers() != null && trip.getPassengers().size() == capacity) {
            throw new TripFullException(trip.getTripId());
        }

        PassengerKey key = new PassengerKey(tripId, email);

        User requester = userRepository.findById(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        Passenger newPassenger = new Passenger();
        newPassenger.setPassengerKey(key);
        newPassenger.setUser(requester);
        newPassenger.setTrip(trip);
        passengerRepository.save(newPassenger);

        repository.deleteById(new TripRequestKey(tripId, email));

        //if this was the last passenger slot
        if(trip.getPassengers().size()- capacity == 1)
            repository.deleteByTrip(tripId);
    }
}
