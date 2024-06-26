package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.entity.rescue.RescueCall;
import com.thangkl2420.server_ducky.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RescueCallRepository extends JpaRepository<RescueCall, Integer> {
    @Query("SELECT rc FROM RescueCall rc " +
            "JOIN UserRescueCall urc ON rc.id = urc.id.userId " +
            "JOIN User u ON urc.id.userId = u.id " +
            "WHERE u.id = :id")
    Optional<RescueCall> findAllById(Integer id);

    @Query("SELECT urc.user FROM UserRescueCall urc WHERE urc.id.rescueCallId = :id AND urc.isCreator = false")
    List<User> findConfirmUsersById(Integer id);

    @Query("SELECT urc.user FROM UserRescueCall urc WHERE urc.id.rescueCallId = :id AND urc.isCreator = true ORDER BY urc.isCreator DESC LIMIT 1")
    Optional<User> findCreateUserById(Integer id);

    @Query("SELECT p.user FROM Participate p WHERE p.rescueCall.id = :id")
    Optional<User> findRescuerById(Integer id);

    @Query("SELECT urc.rescueCall FROM UserRescueCall urc WHERE urc.id.userId = :id AND urc.isCreator = true")
    List<RescueCall> findRescueCallById(Integer id);
}
