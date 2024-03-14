package com.thangkl2420.server_ducky.entity;

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
@Table(name = "rescue_type")
public class RescueType {
    @Id
    @GeneratedValue
    public Integer id;

    @Column(unique = true)
    public String nameRescueType;
}
