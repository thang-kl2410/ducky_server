package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.dto.rescue.ParticipateId;
import com.thangkl2420.server_ducky.entity.rescue.Participate;
import com.thangkl2420.server_ducky.entity.rescue.RescueCall;
import com.thangkl2420.server_ducky.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT p.user FROM Participate p WHERE p.id.rescueCallId = :id")
    List<User> findParticipateUsers(Integer id);

    @Query("SELECT p.user FROM Participate p WHERE p.distance = (SELECT MIN(p2.distance) FROM Participate p2) AND p.id.rescueCallId = :id")
    Optional<User> findUserWithMinimumDistance(Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE Participate p SET p.isFinish = true WHERE p.id.rescueCallId = :id")
    void updateIsFinishToTrueByRescueCallId(Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE RescueCall rc SET rc.isFinish = true WHERE rc.id = :id")
    void finishRescueCall(Integer id);

    @Query("SELECT p.rescueCall FROM Participate p WHERE p.id.userId = :id AND p.isFinish = false ORDER BY p.distance DESC LIMIT 1")
    Optional<RescueCall> findMyCurrentRescueCall(Integer id);
}
