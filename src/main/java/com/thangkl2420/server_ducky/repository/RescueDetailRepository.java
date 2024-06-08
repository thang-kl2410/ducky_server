package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.dto.rescue.RescueDetailId;
import com.thangkl2420.server_ducky.entity.rescue.RescueDetail;
import com.thangkl2420.server_ducky.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RescueDetailRepository extends JpaRepository<RescueDetail, RescueDetailId> {
    @Query("SELECT su.user.idDevice FROM SpecializationUser su " +
            "JOIN RescueType rt ON su.id.rescueTypeId = rt.id " +
            "JOIN RescueDetail rd ON rt.id = rd.id.rescueTypeId " +
            "JOIN Rescue r ON rd.id.rescueId = r.id " +
            "WHERE r.id = :id")
    List<String> findDevicesUserByRescueDetail(Integer id);

    @Query("SELECT su.user.idDevice FROM SpecializationUser su " +
            "JOIN RescueType rt ON su.id.rescueTypeId = rt.id " +
            "JOIN RescueDetail rd ON rt.id = rd.id.rescueTypeId " +
            "JOIN Rescue r ON rd.id.rescueId = r.id " +
            "WHERE r.id = :id AND su.user.idDevice IS NOT NULL")
    List<String> findUserByRescueDetail(Integer id);
    @Query("SELECT su.user FROM SpecializationUser su " +
            "JOIN RescueType rt ON su.id.rescueTypeId = rt.id " +
            "JOIN RescueDetail rd ON rt.id = rd.id.rescueTypeId " +
            "JOIN Rescue r ON rd.id.rescueId = r.id " +
            "WHERE r.id = :id AND su.user.idDevice IS NOT NULL")
    List<User> findCurrentUserByRescueDetail(Integer id);

    @Query("SELECT rd FROM RescueDetail rd " +
            "JOIN RescueType rt ON rd.id.rescueTypeId = rt.id " +
            "WHERE rt.id = :id")
    List<RescueDetail> getAllByType(Integer id);

    @Query("SELECT su.user FROM SpecializationUser su " +
            "JOIN RescueType rt ON su.id.rescueTypeId = rt.id " +
            "WHERE rt.id = :id")
    List<User> getAllUserByType(Integer id);


}
