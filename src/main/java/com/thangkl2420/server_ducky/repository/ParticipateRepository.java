package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.dto.rescue.ParticipateId;
import com.thangkl2420.server_ducky.entity.rescue.Participate;
import com.thangkl2420.server_ducky.entity.rescue.RescueCall;
import com.thangkl2420.server_ducky.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ParticipateRepository extends JpaRepository<Participate, ParticipateId> {
    @Query("SELECT u FROM User u WHERE " +
            "NOT EXISTS " +
            "(SELECT p FROM Participate p WHERE p.user = u AND (p.isFinish IS NULL OR p.isFinish = false)) "+
            "AND EXISTS (SELECT su.user FROM SpecializationUser su " +
            "JOIN RescueType rt ON su.id.rescueTypeId = rt.id " +
            "JOIN RescueDetail rd ON rt.id = rd.id.rescueTypeId " +
            "JOIN Rescue r ON rd.id.rescueId = r.id " +
            "WHERE r.id = :id AND su.user.idDevice IS NOT NULL)")
    List<User> findFreeUsers(Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.userAction.id = 1 " +
            "WHERE u.id IN (SELECT p.user.id FROM Participate p WHERE p.id.rescueCallId = :id)")
    void finishParticipateUsers(Integer id);

    @Query("SELECT p.user FROM Participate p WHERE p.distance = (SELECT MIN(p2.distance) FROM Participate p2 WHERE p2.id.rescueCallId = :id)")
    Optional<User> findUserWithMinimumDistance(Integer id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Participate p WHERE p.id.rescueCallId = :id AND p.user.id != :rescuer")
    void deleteUserParticipate(Integer id, Integer rescuer);

    @Modifying
    @Transactional
    @Query("UPDATE Participate p SET p.isFinish = true WHERE p.id.rescueCallId = :id")
    void updateIsFinishToTrueByRescueCallId(Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE RescueCall rc SET rc.isFinish = true WHERE rc.id = :id")
    void finishRescueCall(Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.userAction.id = 1 " +
            "WHERE u.id IN (SELECT p.user.id FROM Participate p WHERE p.id.rescueCallId = :id AND u.userAction.id != 1)")
    void updateUserParticipate(Integer id);

    @Query("SELECT p.rescueCall FROM Participate p " +
            "WHERE p.id.userId = :id AND p.isFinish = false ORDER BY p.distance DESC LIMIT 1")
    Optional<RescueCall> findMyCurrentRescueCall(Integer id);

    @Query("SELECT urc.rescueCall FROM UserRescueCall urc " +
            "WHERE urc.id.userId = :id AND urc.rescueCall.isFinish = false ORDER BY urc.id DESC LIMIT 1")
    Optional<RescueCall> findMyCurrentRescueCall2(Integer id);
}
