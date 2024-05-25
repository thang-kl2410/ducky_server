package com.thangkl2420.server_ducky.dto.rescue;

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
public class UserRescueCallId implements Serializable {
    @Column(name = "rescue_call_id")
    private Integer rescueCallId;
    @Column(name = "user_id")
    private Integer userId;
}