package com.thangkl2420.server_ducky.dto.rescue;

import com.thangkl2420.server_ducky.entity.rescue.RescueCall;
import com.thangkl2420.server_ducky.entity.user.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RescueCallResponseDto {
    private RescueCall rescueCall;
    private List<User> rescuers;
}
