package com.thangkl2420.server_ducky.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thangkl2420.server_ducky.dto.user.UserNotificationId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_notification")
public class UserNotification {
    @EmbeddedId
    private UserNotificationId id;
    private Integer isSeen;

    @ManyToOne
    @JoinColumn(name = "notification_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DuckyNotification notification;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;
}
