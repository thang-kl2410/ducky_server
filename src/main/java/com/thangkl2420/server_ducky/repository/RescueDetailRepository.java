package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.dto.RescueDetailId;
import com.thangkl2420.server_ducky.entity.RescueCall;
import com.thangkl2420.server_ducky.entity.RescueDetail;
import com.thangkl2420.server_ducky.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RescueDetailRepository extends JpaRepository<RescueDetail, RescueDetailId> {
    @Query("SELECT su.user.idDevice FROM SpecializationUser su " +
            "JOIN RescueType rt ON su.id.rescueTypeId = rt.id " +
            "JOIN RescueDetail rd ON rt.id = rd.id.rescueType " +
            "JOIN Rescue r ON rd.id.rescueId = r.id " +
            "WHERE r.id = :id")
    List<String> findDevicesUserByRescueDetail(Integer id);

    @Query("SELECT su.user FROM SpecializationUser su " +
            "JOIN RescueType rt ON su.id.rescueTypeId = rt.id " +
            "JOIN RescueDetail rd ON rt.id = rd.id.rescueType " +
            "JOIN Rescue r ON rd.id.rescueId = r.id " +
            "WHERE r.id = :id")
    List<String> findUserByRescueDetail(Integer id);
}
