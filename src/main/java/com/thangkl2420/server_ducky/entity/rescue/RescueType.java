package com.thangkl2420.server_ducky.entity.rescue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thangkl2420.server_ducky.entity.user.SpecializationUser;
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
@Table(name = "rescue_type")
public class RescueType {
    @Id
    @GeneratedValue
    public Integer id;

    @Column(unique = true)
    public String nameRescueType;
    @OneToMany(mappedBy = "rescueType", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<SpecializationUser> specializationUsers;
}
