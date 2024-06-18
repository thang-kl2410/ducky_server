package com.thangkl2420.server_ducky.dto.auth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AuthenticationRequest {

  private String email;
  String password;
}
