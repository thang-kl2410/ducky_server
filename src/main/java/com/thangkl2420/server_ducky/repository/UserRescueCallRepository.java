package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.dto.rescue.UserRescueCallId;
import com.thangkl2420.server_ducky.entity.rescue.UserRescueCall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRescueCallRepository extends JpaRepository<UserRescueCall, UserRescueCallId> {
}
