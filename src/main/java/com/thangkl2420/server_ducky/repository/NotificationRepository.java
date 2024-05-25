package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.entity.user.DuckyNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<DuckyNotification, Integer> {

}
