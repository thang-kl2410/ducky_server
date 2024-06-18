package com.thangkl2420.server_ducky.dto.rescue;

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
public class RescueDetailId implements Serializable {
    @Column(name = "rescue_id")
    private Integer rescueId;

    @Column(name = "rescue_type_id")
    private Integer rescueTypeId;

    @Column(name = "rescue_personnel_id")
    private Integer rescuePersonnelId;
}
