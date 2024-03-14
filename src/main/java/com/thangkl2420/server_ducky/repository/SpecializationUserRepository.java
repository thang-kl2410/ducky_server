package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.dto.SpecializationUserId;
import com.thangkl2420.server_ducky.entity.Specialization;
import com.thangkl2420.server_ducky.entity.SpecializationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpecializationUserRepository extends JpaRepository<SpecializationUser, SpecializationUserId> {
    @Query(value = "select su.specialization from SpecializationUser su where su.id.userId = :id ")
    List<Specialization> findAllByUser(Integer id);
    @Query(value = "select su.user from SpecializationUser su where su.id.specializationId = :id ")
    List<Specialization> findAllBySpecialization(Integer id);
}
