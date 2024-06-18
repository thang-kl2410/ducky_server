package com.thangkl2420.server_ducky.dto.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserNotificationId implements Serializable {
    @Column(name = "notification_id")
    private Integer notificationId;
    @Column(name = "user_id")
    private Integer userId;
}