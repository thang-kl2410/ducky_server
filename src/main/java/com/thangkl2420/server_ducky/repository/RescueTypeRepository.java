package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.entity.RescueType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RescueTypeRepository extends JpaRepository<RescueType, Integer> {
    @Query("SELECT rt FROM RescueType rt " +
            "JOIN SpecializationUser su ON rt.id = su.id.rescueTypeId " +
            "JOIN User u ON su.id.userId = u.id " +
            "WHERE u.id = :id")
    Optional<RescueType> findAllById(Integer id);
}
