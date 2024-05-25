package com.thangkl2420.server_ducky.entity.rescue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thangkl2420.server_ducky.dto.rescue.ParticipateId;
import com.thangkl2420.server_ducky.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "participate")
public class Participate implements Serializable {
    @JsonIgnore
    @EmbeddedId
    private ParticipateId id;
    private Boolean isFinish;
    private double distance;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "rescue_call_id", referencedColumnName = "id", insertable = false, updatable = false)
    private RescueCall rescueCall;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;
}
