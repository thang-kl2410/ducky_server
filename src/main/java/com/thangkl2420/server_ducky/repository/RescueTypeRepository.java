package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.entity.rescue.RescueType;
import com.thangkl2420.server_ducky.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RescueTypeRepository extends JpaRepository<RescueType, Integer> {
    @Query("SELECT rt FROM RescueType rt " +
            "JOIN SpecializationUser su ON rt.id = su.id.rescueTypeId " +
            "JOIN User u ON su.id.userId = u.id " +
            "WHERE u.id = :id")
    Optional<RescueType> findAllById(Integer id);

//    @Query("DELETE FROM SpecializationUser su WHERE su.user = :user")
//    void deleteByUser(User user);

    @Query("SELECT su.rescueType FROM SpecializationUser su WHERE su.id.userId = :id")
    List<RescueType> getAllByUser(Integer id);
}
