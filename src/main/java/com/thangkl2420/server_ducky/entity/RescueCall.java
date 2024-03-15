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
@Table(name = "rescue_call")
public class RescueCall {
    @Id
    @GeneratedValue
    private Integer id;
    boolean isCreating;
    String description;
    String image;
    String video;
    double rating;
    double longitude;
    double latitude;
    double timestamp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rescue_id")
    public Rescue rescue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rescueState_id")
    public RescueState rescueState;
}
