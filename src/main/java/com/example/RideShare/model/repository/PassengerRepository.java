package com.example.RideShare.model.repository;

import com.example.RideShare.model.entity.Passenger;
import com.example.RideShare.model.keys.PassengerKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, PassengerKey> {
    @Query(value = "select * from passengers where passengerTripId = :tripId", nativeQuery = true)
    List<Passenger> findAllByTripId(@Param("tripId") long tripId);

    @Query(value = "select * from passengers where passengerEmail = :email", nativeQuery = true)
    List<Passenger> findAllByPassengerEmail(@Param("email") String email);

    @Modifying
    @Query(value = "delete from passengers where passengerTripId = :tripId", nativeQuery = true)
    void deleteAllByTripId(@Param("tripId") long tripId);

    @Modifying
    @Query(value = "delete from passengers where passengerEmail = :email", nativeQuery = true)
    void deleteAllByEmail(@Param("email") String email);

    @Modifying
    @Query(value = "delete from passengers where passengerTripId in " +
            "(select tripId from vehicles join trips where vehicles.licensePlate = :licensePlate)", nativeQuery = true)
    void deleteAllByTripVehicle(@Param("licensePlate") String licensePlate);

    @Modifying
    @Query(value = "delete from passengers where passengerTripId in " +
            "(select tripId from trips where driverEmail = :driverEmail)", nativeQuery = true)
    void deleteAllByDriverEmail(@Param("driverEmail") String driverEmail);
}
