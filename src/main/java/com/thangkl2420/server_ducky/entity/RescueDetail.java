package com.thangkl2420.server_ducky.entity;

import com.thangkl2420.server_ducky.dto.RescueDetailId;
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
    @EmbeddedId
    private RescueDetailId id;

    @ManyToOne
    @JoinColumn(name = "rescue_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    private RescueType rescueType;
    @ManyToOne
    @JoinColumn(name = "rescue_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Rescue rescue;
}
