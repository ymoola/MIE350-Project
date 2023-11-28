package com.example.RideShare.model.repository;

import com.example.RideShare.model.entity.TripRequest;
import com.example.RideShare.model.keys.TripRequestKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripRequestRepository extends JpaRepository<TripRequest, TripRequestKey> {

    // search requests by trip
    @Query(value= "SELECT *" +
            "FROM tripRequests tr" +
            "WHERE tr.tripId = :tripId", nativeQuery = true)
    List<TripRequest> getByTrip(@Param("tripId") long tripId);

    // search requests by passanger/request maker
    @Query(value="SELECT *" +
            "FROM tripRequests tr" +
            "WHERE tr.userEmail = :userEmail", nativeQuery = true)
    List<TripRequest> getByUser(@Param("userEmail") String userEmail);

    // search requests by driver - see all requests for a certain driver
    @Query(value = "SELECT *" +
            "FROM tripRequests tr join trip t" +
            "WHERE tr.tripId = t.tripId" +
            "AND driverEmail = :driverEmailSearch", nativeQuery = true)
    List<TripRequest> getByDriver(@Param("driverEmailSearch") String driverEmail);

    // delete requests



}
