package com.thangkl2420.server_ducky.controller;

import com.thangkl2420.server_ducky.dto.auth.ResetPasswordRequest;
import com.thangkl2420.server_ducky.exception.ApiResponse;
import com.thangkl2420.server_ducky.exception.InternalServerErrorException;
import com.thangkl2420.server_ducky.service.LogoutService;
import com.thangkl2420.server_ducky.dto.auth.AuthenticationRequest;
import com.thangkl2420.server_ducky.entity.auth.AuthenticationResponse;
import com.thangkl2420.server_ducky.service.AuthenticationService;
import com.thangkl2420.server_ducky.dto.auth.RegisterRequest;
import com.thangkl2420.server_ducky.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;

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
  public ResponseEntity<Boolean> logout(
          HttpServletRequest request,
          HttpServletResponse response,
          Principal connectedUser
  ) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    logoutService.logout(request, response,authentication);
    userService.saveDeviceToken("", connectedUser);
    userService.updateState(2, connectedUser);
    return ResponseEntity.ok(true);
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }

  @PostMapping("/reset-password")
  public ResponseEntity<Boolean> resetPassword(
          @RequestBody ResetPasswordRequest request
  ) {
    try{
      Boolean isExits = service.isEmailExist(request.getEmail());
      if(isExits){
        Boolean isOke = service.resetPassword(request);
        return ResponseEntity.ok(isOke);
      } else {
        throw new InternalServerErrorException("Email is not registered!");
      }
    } catch (Exception e){
      throw new InternalServerErrorException("Processing error: " + e.getMessage());
    }
  }

  @GetMapping("/email-exist")
  public ResponseEntity<Boolean> isEmailExist(@Param(value = "email") String email) {
    try{
      Boolean isOke = service.isEmailExist(email);
      return ResponseEntity.ok(isOke);
    } catch (Exception e){
      throw new InternalServerErrorException("Lỗi xử lý: " + e.getMessage());
    }
  }
}
