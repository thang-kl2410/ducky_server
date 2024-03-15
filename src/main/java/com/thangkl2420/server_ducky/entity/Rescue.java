package com.thangkl2420.server_ducky.entity;

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
@Table(name = "rescue")
public class Rescue {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String rescueName;
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "rescue")
    private List<RescueCall> rescueCalls;
}
