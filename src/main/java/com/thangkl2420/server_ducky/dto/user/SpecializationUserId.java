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
public class SpecializationUserId implements Serializable {
    @Column(name = "rescueType_id")
    private Integer rescueTypeId;
    @Column(name = "user_id")
    private Integer userId;
}
