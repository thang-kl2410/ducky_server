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
public class SpecializationUserId implements Serializable {
//    @Column(name = "specialization_id")
//    private Integer specializationId;
    @Column(name = "rescueType_id")
    private Integer rescueTypeId;
    @Column(name = "user_id")
    private Integer userId;
}
