package com.thangkl2420.server_ducky.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thangkl2420.server_ducky.dto.user.FollowingId;
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
@Table(name = "following")
public class Following {
    @EmbeddedId
    private FollowingId id;

    @ManyToOne
    @JoinColumn(name = "follower_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User follower;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;
}
