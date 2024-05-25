package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.dto.user.UserNotificationId;
import com.thangkl2420.server_ducky.entity.user.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNotificationRepository extends JpaRepository<UserNotification, UserNotificationId> {

}
