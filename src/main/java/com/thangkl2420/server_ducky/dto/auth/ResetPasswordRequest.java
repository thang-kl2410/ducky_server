package com.thangkl2420.server_ducky.dto.auth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ResetPasswordRequest {
    private String email;
    private String password;
}
