package com.example.RideShare.controller;

import com.example.RideShare.controller.exceptions.TripNotFoundException;
import com.example.RideShare.controller.exceptions.UserNotFoundException;
import com.example.RideShare.controller.exceptions.VehicleNotFoundException;
import com.example.RideShare.model.entity.User;
import com.example.RideShare.model.entity.Vehicle;
import com.example.RideShare.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private final UserRepository repository;

    public UserController(UserRepository repository) {this.repository = repository;}

    @GetMapping
    List<User> retrieveAllUsers(){return repository.findAll();}


    @GetMapping("/{email}")
    User getByEmail(@PathVariable("email") String email){
        return repository.findById(email)
                .orElseThrow(
                        ()-> new UserNotFoundException(email)
                );
    }

    //this doesn't need a dto because it has no foreign keys on creation
    @PostMapping
    User createUser(@RequestBody User newUser){
        return repository.save(newUser);
    }

    //this method allows the user to update their email, password, and everything else
    @PutMapping("/{email}")
    User updateUser(@RequestBody User updatedUser, @PathVariable("email") String email){
        return repository.findById(email)
                .map(user -> {
                    user.setEmail(updatedUser.getEmail());
                    user.setPassword(updatedUser.getPassword());
                    user.setPhoneNumber(updatedUser.getPhoneNumber());
                    user.setAddress(updatedUser.getAddress());
                    user.setPostalCode(updatedUser.getPostalCode());
                    user.setFirstName(updatedUser.getFirstName());
                    user.setLastName(updatedUser.getLastName());
                    return repository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(email)); // Custom exception
    }
}
