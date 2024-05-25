package com.thangkl2420.server_ducky.entity.rescue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thangkl2420.server_ducky.dto.rescue.RescueDetailId;
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
@Table(name = "rescue_detail")
public class RescueDetail {
    @JsonIgnore
    @EmbeddedId
    private RescueDetailId id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "rescue_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    private RescueType rescueType;
    @ManyToOne
    @JoinColumn(name = "rescue_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Rescue rescue;
    @ManyToOne
    @JoinColumn(name = "rescue_personnel_id", referencedColumnName = "id", insertable = false, updatable = false)
    private RescuePersonnel rescuePersonnel;
}
