package com.thangkl2420.server_ducky.dto;

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
public class RescueDetailId implements Serializable {
    @Column(name = "rescue_id")
    private Integer rescueId;
    @Column(name = "rescue_type_id")
    private Integer rescueType;
}
