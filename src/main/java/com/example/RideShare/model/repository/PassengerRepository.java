package com.example.RideShare.model.repository;

import com.example.RideShare.model.entity.Passenger;
import com.example.RideShare.model.keys.PassengerKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, PassengerKey> {
}
