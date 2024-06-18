package com.thangkl2420.server_ducky.dto.rescue;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ParticipateId {
    @Column(name = "rescue_call_id")
    private Integer rescueCallId;

    @Column(name = "user_id")
    private Integer userId;
}
