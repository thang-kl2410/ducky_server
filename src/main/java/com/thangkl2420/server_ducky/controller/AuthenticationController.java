package com.thangkl2420.server_ducky.controller;

import com.thangkl2420.server_ducky.service.LogoutService;
import com.thangkl2420.server_ducky.dto.AuthenticationRequest;
import com.thangkl2420.server_ducky.entity.AuthenticationResponse;
import com.thangkl2420.server_ducky.service.AuthenticationService;
import com.thangkl2420.server_ducky.dto.RegisterRequest;
import com.thangkl2420.server_ducky.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationService service;
  private final LogoutService logoutService;
  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(
          HttpServletRequest request,
          HttpServletResponse response,
          Principal connectedUser
  ) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    logoutService.logout(request, response,authentication);
    userService.saveDeviceToken("", connectedUser);
    userService.updateState(2, connectedUser);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }
}
