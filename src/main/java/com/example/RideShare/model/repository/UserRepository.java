package com.example.RideShare.model.repository;

import com.example.RideShare.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    @Query(value = "select * from users u where lower(u.firstName) like lower(concat('%', :searchTerm, '%')) or lower(u.lastName) like lower(concat('%', :searchTerm, '%'))", nativeQuery = true)
    List<User> searchByName(@Param("searchTerm") String searchTerm);
}
