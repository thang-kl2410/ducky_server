package com.thangkl2420.server_ducky.dto.auth;

import com.thangkl2420.server_ducky.entity.auth.Role;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RegisterRequest {
  private String firstname;
  private String lastname;
  private String email;
  private String password;
  private Role role;
}
