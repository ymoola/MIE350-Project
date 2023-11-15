package com.example.RideShare.model.repository;

import com.example.RideShare.model.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query(value = "select t.* from users u join trips t on u.email = t.driverEmail where lower(u.firstName) like lower(concat('%', :searchTerm, '%')) or lower(u.lastName) like lower(concat('%', :searchTerm, '%'))", nativeQuery = true)
    List<Trip> searchByDriver(@Param("searchTerm") String searchTerm);

    @Query(value = "select * from trips where driverEmail = :email", nativeQuery = true)
    List<Trip> getDriverTrips(@Param("email") String email);

    @Modifying
    @Query(value = "delete from trips where driverEmail = :email", nativeQuery = true)
    void deleteTripsByDriver(@Param("email") String email);
}
