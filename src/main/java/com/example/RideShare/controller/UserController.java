package com.example.RideShare.controller;

import com.example.RideShare.controller.exceptions.UserNotFoundException;
import com.example.RideShare.model.entity.User;
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
}
