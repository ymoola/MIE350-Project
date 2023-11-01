package com.example.RideShare.controller;

import com.example.RideShare.controller.dto.VehicleDto;
import com.example.RideShare.controller.exceptions.UserNotFoundException;
import com.example.RideShare.controller.exceptions.VehicleNotFoundException;
import com.example.RideShare.model.entity.User;
import com.example.RideShare.model.entity.Vehicle;
import com.example.RideShare.model.repository.UserRepository;
import com.example.RideShare.model.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private final VehicleRepository repository;

    @Autowired
    private UserRepository userRepository;

    public VehicleController(VehicleRepository repository) {this.repository = repository;}

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return repository.findAll();
    }

    @PostMapping
    public Vehicle createVehicle(@RequestBody VehicleDto vehicleDto){
        Vehicle newVehicle = new Vehicle();
        //might have to add notNull checks on these setters
        newVehicle.setLicensePlate(vehicleDto.getLicensePlate());
        newVehicle.setMake(vehicleDto.getMake());
        newVehicle.setModel(vehicleDto.getModel());
        newVehicle.setType(vehicleDto.getType());
        newVehicle.setPassengerSeats(vehicleDto.getPassengerSeats());
        newVehicle.setColor(vehicleDto.getColor());
        User owner = userRepository.findById(vehicleDto.getOwnerEmail()).orElseThrow(
                () -> new UserNotFoundException(vehicleDto.getOwnerEmail()));
        newVehicle.setOwner(owner);
        return repository.save(newVehicle);
    }

    @GetMapping("/{licensePlate}")
    public Vehicle getVehicleByLicensePlate(@PathVariable String licensePlate) {
        return repository.findById(licensePlate)
                .orElseThrow(() -> new VehicleNotFoundException(licensePlate)); // Custom exception
    }

    @PutMapping("/{licensePlate}")
    public Vehicle updateVehicle(@RequestBody VehicleDto updatedVehicleDto, @PathVariable String licensePlate) {
        return repository.findById(licensePlate)
                .map(vehicle -> {
                    //might have to add notNull checks on these setters
                    vehicle.setMake(updatedVehicleDto.getMake());
                    vehicle.setModel(updatedVehicleDto.getModel());
                    vehicle.setType(updatedVehicleDto.getType());
                    vehicle.setPassengerSeats(updatedVehicleDto.getPassengerSeats());
                    vehicle.setColor(updatedVehicleDto.getColor());
                    User owner = userRepository.findById(updatedVehicleDto.getOwnerEmail()).orElseThrow(
                            () -> new UserNotFoundException(updatedVehicleDto.getOwnerEmail()));
                    vehicle.setOwner(owner);
                    return repository.save(vehicle);
                })
                .orElseThrow(() -> new VehicleNotFoundException(licensePlate)); // Custom exception
    }

    @DeleteMapping("/{licensePlate}")
    public void deleteVehicle(@PathVariable String licensePlate) {
        repository.deleteById(licensePlate);
    }
}
