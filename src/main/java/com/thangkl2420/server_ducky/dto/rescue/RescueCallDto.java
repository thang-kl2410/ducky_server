package com.thangkl2420.server_ducky.dto.rescue;

import com.thangkl2420.server_ducky.entity.rescue.RescueCall;
import com.thangkl2420.server_ducky.entity.user.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RescueCallDto {
    private RescueCall rescueCall;
    private User createUser;
    private User rescuer;
}
