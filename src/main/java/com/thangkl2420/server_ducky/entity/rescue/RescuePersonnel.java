package com.thangkl2420.server_ducky.entity.rescue;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "rescue_personnel")
public class RescuePersonnel {
    @Id
    private Integer id;
    @Column(unique = true)
    private String code;
    @JsonIgnore
    @OneToMany(mappedBy = "rescuePersonnel")
    private List<RescueCall> rescueCalls;
}
