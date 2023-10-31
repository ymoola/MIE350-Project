package com.example.RideShare.controller;

import com.example.RideShare.model.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class VehicleController {

    @Autowired
    private final VehicleRepository repository;

    public VehicleController(VehicleRepository repository) {this.repository = repository;}

    
}
