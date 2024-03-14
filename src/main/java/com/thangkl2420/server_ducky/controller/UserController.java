package com.thangkl2420.server_ducky.controller;

import com.thangkl2420.server_ducky.dto.ChangePasswordRequest;
import com.thangkl2420.server_ducky.dto.UpdateProfileRequest;
import com.thangkl2420.server_ducky.entity.User;
import com.thangkl2420.server_ducky.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("change-password")
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/current-user")
    public ResponseEntity<User> getCurrentUser(Principal connectedUser) {
        return ResponseEntity.ok(
                (User) ((UsernamePasswordAuthenticationToken) connectedUser)
                        .getPrincipal()
        );
    }

    @GetMapping("/device-token")
    public ResponseEntity<?> getDeviceToken(Principal connectedUser, @Param(value = "device_token") String deviceToken) {
        service.saveDeviceToken(deviceToken, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update-profile")
    public ResponseEntity<?> updateProfile(Principal connectedUser, @RequestBody UpdateProfileRequest request) {
        service.updateProfile(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/update-state")
    public ResponseEntity<?> updateState(Principal connectedUser, @Param(value = "state") Integer state){
        service.updateState(state, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/location")
    public ResponseEntity<?> setLocation(
            Principal connectedUser,
            @Param(value = "longitude") double longitude,
            @Param(value = "latitude") double latitude){
        service.setLocation(longitude, latitude, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/id-user")
    public ResponseEntity<User> getById(
            @Param(value = "id") Integer id
    ){
        User user = service.getById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/search-by-name")
    public ResponseEntity<List<User>> searchByName(
            @Param(value = "keyword") String keyword
    ){
        List<User> users = service.searchByName(keyword);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<User>> getAllUser(Principal connectedUser){
        List<User> users = service.getAllUser(connectedUser);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/get-near-people")
    public ResponseEntity<List<User>> getNearPeople(Principal connectedUser){
        List<User> users = service.getNearPeople(connectedUser);
        return ResponseEntity.ok(users);
    }
}
