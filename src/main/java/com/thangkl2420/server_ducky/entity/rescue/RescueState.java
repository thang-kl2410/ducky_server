package com.thangkl2420.server_ducky.entity.rescue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thangkl2420.server_ducky.entity.rescue.RescueCall;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rescue_state")
public class RescueState {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String rescueState;

    @JsonIgnore
    @OneToMany(mappedBy = "rescueState")
    private List<RescueCall> rescueCalls;
}
