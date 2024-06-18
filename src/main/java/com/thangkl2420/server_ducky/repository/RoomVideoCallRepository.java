package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.entity.chat.RoomVideoCall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomVideoCallRepository extends JpaRepository<RoomVideoCall, String> {
}
