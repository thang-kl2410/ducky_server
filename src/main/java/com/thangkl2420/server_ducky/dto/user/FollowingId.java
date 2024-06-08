package com.thangkl2420.server_ducky.dto.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowingId implements Serializable {
    @Column(name = "follower_id")
    private Integer followerId;
    @Column(name = "user_id")
    private Integer userId;
}