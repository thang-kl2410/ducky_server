package com.thangkl2420.server_ducky.entity;

import com.thangkl2420.server_ducky.dto.SpecializationUserId;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "specialization_user")
public class SpecializationUser {
    @EmbeddedId
    private SpecializationUserId id;

    @ManyToOne
    @JoinColumn(name = "rescueType_id", referencedColumnName = "id", insertable = false, updatable = false)
    private RescueType rescueType;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;
}
