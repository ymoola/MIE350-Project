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

    // get by user (requester) --> TODO: needs authorization
    @GetMapping("/byRequester/{email}")
    List<TripRequest> getByRequester(@PathVariable String email) {
        return repository.getByUser(email);
    }

    //by driver
    @GetMapping("/byDriver/{email}")
    List<TripRequest> getByTripDriver(@PathVariable String email) {return repository.getByDriver(email); }
    @PostMapping //defaults to the path above TODO: finish the post mapping
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

    // TODO: the email here has to match the authentication
    @PutMapping("/{tripId}/{email}")
    TripRequest updateRequest(@PathVariable long tripId, @PathVariable String email, @RequestBody TripRequestDto tripRequestDto){
        TripRequestKey key = new TripRequestKey(tripId, email);
        TripRequest tripRequest = repository.findById(key)
                .orElseThrow(() -> new TripRequestNotFoundException(tripId, email));
        tripRequest.setMessage(tripRequestDto.getMessage());
        tripRequest.setDateIssued(new Date());
        return repository.save(tripRequest);
    }


    @DeleteMapping("/decline/{tripId}/{email}") //email of the requester
    void declineRequest(@PathVariable long tripId, @PathVariable String email){repository.deleteById(new TripRequestKey(tripId,email));}

    @DeleteMapping("accept/{tripId}/{userEmail}")
    void acceptRequest(@PathVariable long tripId, @PathVariable String userEmail){

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException(tripId));

        int capacity = trip.getVehicle().getPassengerSeats();
        //throw exception if already full
        if (trip.getPassengers() != null && trip.getPassengers().size() == capacity) {
            throw new TripFullException(trip.getTripId());
        }
        //if not full we can accept new passanger
        PassengerKey key = new PassengerKey(tripId, userEmail);

        User requester = userRepository.findById(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));

        Passenger newPassenger = new Passenger();
        newPassenger.setPassengerKey(key);
        newPassenger.setUser(requester);
        newPassenger.setTrip(trip);
        passengerRepository.save(newPassenger);

        repository.deleteById(new TripRequestKey(tripId,userEmail));

        //if this was the last passanger, delete all outstanding requests
        if(trip.getPassengers().size()- capacity == 1){
            //this means that there was 1 space for the passanger (trip not updated, still like @start)
            repository.deleteByTrip(tripId);
        }
    }



}
