package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.dto.user.SpecializationUserId;
import com.thangkl2420.server_ducky.entity.rescue.RescueType;
import com.thangkl2420.server_ducky.entity.user.SpecializationUser;
import com.thangkl2420.server_ducky.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpecializationUserRepository extends JpaRepository<SpecializationUser, SpecializationUserId> {
    @Query(value = "select su.rescueType from SpecializationUser su where su.user = :user ")
    List<RescueType> findAllByUser(User user);
    @Modifying
    @Query(value = "DELETE FROM SpecializationUser su WHERE su.user = :user")
    void deleteAllByUser(User user);
}
