package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.dto.user.UserNotificationId;
import com.thangkl2420.server_ducky.entity.post.Post;
import com.thangkl2420.server_ducky.entity.user.DuckyNotification;
import com.thangkl2420.server_ducky.entity.user.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserNotificationRepository extends JpaRepository<UserNotification, UserNotificationId> {
    @Query("SELECT un.notification FROM UserNotification un WHERE un.id.userId = :id")
    List<DuckyNotification> findAllByUserId(Integer id);

    @Query("SELECT n FROM DuckyNotification n WHERE " +
            "(:startTime IS NULL OR n.timestamp >= :startTime) " +
            "AND (:endTime IS NULL OR n.timestamp <= :endTime) " +
            "AND (n.content LIKE %:keyWord% OR n.title LIKE %:keyWord%) " +
            "ORDER BY n.timestamp DESC")
    List<DuckyNotification> filterNotification(long startTime, long endTime, String keyWord);
}
