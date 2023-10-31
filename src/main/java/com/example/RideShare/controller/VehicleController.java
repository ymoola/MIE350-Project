package com.example.RideShare.controller;

import com.example.RideShare.model.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private final VehicleRepository repository;

    public VehicleController(VehicleRepository repository) {this.repository = repository;}

    
}
