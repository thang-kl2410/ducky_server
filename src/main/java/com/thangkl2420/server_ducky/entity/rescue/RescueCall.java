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
@Table(name = "rescue_call")
public class RescueCall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonIgnore
    boolean isCreating;
    String description;
//    String image;
//    String video;
    double rating;
    double longitude;
    double latitude;
    Long timestamp;

    @ElementCollection
    @CollectionTable(name = "rescue_images", joinColumns = @JoinColumn(name = "rescue_call_id"))
    @Column(name = "images")
    private List<Integer> images;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rescue_id")
    public Rescue rescue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rescueState_id")
    public RescueState rescueState;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rescuePersonnel_id")
    public RescuePersonnel rescuePersonnel;
}
