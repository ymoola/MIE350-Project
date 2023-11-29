package com.example.RideShare.controller;

import com.example.RideShare.controller.dto.VehicleDto;
import com.example.RideShare.controller.exceptions.UserNotFoundException;
import com.example.RideShare.controller.exceptions.VehicleNotFoundException;
import com.example.RideShare.controller.exceptions.VehicleOwnerIncorrectException;
import com.example.RideShare.controller.exceptions.VehicleWithLicensePlateAlreadyExistsException;
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

@CrossOrigin
@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    @Autowired
    private final VehicleRepository repository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final TripRepository tripRepository;

    @Autowired
    private final PassengerRepository passengerRepository;

    public VehicleController(VehicleRepository repository,
                             UserRepository userRepository,
                             TripRepository  tripRepository,
                             PassengerRepository passengerRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.passengerRepository = passengerRepository;
    }

    @PostMapping
    public Vehicle createVehicle(Authentication authentication, @RequestBody VehicleDto vehicleDto){
        if (repository.existsById(vehicleDto.getLicensePlate()))
            throw new VehicleWithLicensePlateAlreadyExistsException(vehicleDto.getLicensePlate());

        Vehicle newVehicle = new Vehicle();
        newVehicle.setLicensePlate(vehicleDto.getLicensePlate());
        newVehicle.setMake(vehicleDto.getMake());
        newVehicle.setModel(vehicleDto.getModel());
        newVehicle.setType(vehicleDto.getType());
        newVehicle.setPassengerSeats(vehicleDto.getPassengerSeats());
        newVehicle.setColor(vehicleDto.getColor());

        User owner = userRepository.findById(authentication.getName()).orElseThrow(
                () -> new UserNotFoundException(authentication.getName()));
        newVehicle.setOwner(owner);

        return repository.save(newVehicle);
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return repository.findAll();
    }


    @GetMapping("/{licensePlate}")
    public Vehicle getVehicleByLicensePlate(@PathVariable String licensePlate) {
        return repository.findById(licensePlate)
                .orElseThrow(() -> new VehicleNotFoundException(licensePlate)); // Custom exception
    }

    @GetMapping("/getOwnerVehicles/{email}")
    List<Vehicle> getOwnerVehicles(@PathVariable("email") String email){
        return repository.getOwnerVehicles(email);
    }

    @PutMapping("/{licensePlate}")
    @PostAuthorize("returnObject.owner.email == authentication.principal")
    public Vehicle updateVehicle(@RequestBody VehicleDto updatedVehicleDto, @PathVariable String licensePlate) {
        return repository.findById(licensePlate)
                .map(vehicle -> {
                    vehicle.setMake(updatedVehicleDto.getMake());
                    vehicle.setModel(updatedVehicleDto.getModel());
                    vehicle.setType(updatedVehicleDto.getType());
                    vehicle.setPassengerSeats(updatedVehicleDto.getPassengerSeats());
                    vehicle.setColor(updatedVehicleDto.getColor());
                    return repository.save(vehicle);
                })
                .orElseThrow(() -> new VehicleNotFoundException(licensePlate));
    }

    @DeleteMapping("/{licensePlate}")
    public void deleteVehicle(Authentication authentication, @PathVariable String licensePlate) {

        if (!repository.existsById(licensePlate))
            return;

        Vehicle vehicle = repository.findById(licensePlate)
                        .orElseThrow(()-> new VehicleNotFoundException(licensePlate));

        if (!vehicle.getOwner().getEmail().equals(authentication.getName()))
            throw new VehicleOwnerIncorrectException(authentication.getName(), licensePlate);

        repository.deleteById(licensePlate);
    }
}