package com.thangkl2420.server_ducky.entity.rescue;

import com.thangkl2420.server_ducky.dto.rescue.UserRescueCallId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thangkl2420.server_ducky.entity.user.User;
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
@Table(name = "user_rescue_call")
public class UserRescueCall {
    @EmbeddedId
    private UserRescueCallId id;
    private boolean isCreator;

    @ManyToOne
    @JoinColumn(name = "rescue_call_id", referencedColumnName = "id", insertable = false, updatable = false)
    private RescueCall rescueCall;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;
}
