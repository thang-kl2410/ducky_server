package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.dto.FilterRequest;
import com.thangkl2420.server_ducky.dto.user.UserNotificationId;
import com.thangkl2420.server_ducky.entity.post.Post;
import com.thangkl2420.server_ducky.entity.user.DuckyNotification;
import com.thangkl2420.server_ducky.entity.user.UserNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserNotificationRepository extends JpaRepository<UserNotification, UserNotificationId> {
    @Query("SELECT un.notification FROM UserNotification un WHERE un.id.userId = :id ORDER BY un.notification.timestamp DESC")
    List<DuckyNotification> findAllByUserId(Integer id);

    @Query("SELECT un.notification FROM UserNotification un WHERE " +
            "un.id.userId = :id " +
            "AND (:startTime IS NULL OR un.notification.timestamp >= :startTime) " +
            "AND (:endTime IS NULL OR un.notification.timestamp <= :endTime) " +
            "AND (un.notification.content LIKE %:keyword% OR un.notification.title LIKE %:keyword%) " +
            "ORDER BY un.notification.timestamp DESC")
    Page<DuckyNotification> filterNotification(Pageable pageable, long startTime, long endTime, String keyword, Integer id);
}
