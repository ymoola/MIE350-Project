package com.example.RideShare.model.repository;

import com.example.RideShare.model.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    @Query(value = "select * from vehicles where ownerEmail = :email", nativeQuery = true)
    List<Vehicle> getOwnerVehicles(@Param("email") String email);

    @Modifying
    @Query(value = "delete from vehicles where ownerEmail = :email", nativeQuery = true)
    void deleteOwnerVehicles(@Param("email") String email);
}
