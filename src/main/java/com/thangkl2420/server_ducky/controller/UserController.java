package com.thangkl2420.server_ducky.controller;

import com.thangkl2420.server_ducky.dto.auth.ChangePasswordRequest;
import com.thangkl2420.server_ducky.dto.user.UpdateProfileRequest;
import com.thangkl2420.server_ducky.entity.user.User;
import com.thangkl2420.server_ducky.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("change-password")
    public ResponseEntity<User> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        return ResponseEntity.ok(service.changePassword(request, connectedUser));
    }

    @GetMapping("/current-user")
    public ResponseEntity<User> getCurrentUser(Principal connectedUser) {
        return ResponseEntity.ok(
                (User) ((UsernamePasswordAuthenticationToken) connectedUser)
                        .getPrincipal()
        );
    }

    @GetMapping("/device-token")
    public ResponseEntity<User> getDeviceToken(Principal connectedUser, @Param(value = "device_token") String deviceToken) {
        return ResponseEntity.ok(service.saveDeviceToken(deviceToken, connectedUser));
    }

    @PostMapping("/update-profile")
    public ResponseEntity<User> updateProfile(Principal connectedUser, @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok( service.updateProfile(request, connectedUser));
    }

    @GetMapping("/update-state")
    public ResponseEntity<User> updateState(Principal connectedUser, @Param(value = "state") Integer state){
        return ResponseEntity.ok(service.updateState(state, connectedUser));
    }

    @GetMapping("/location")
    public ResponseEntity<User> setLocation(
            Principal connectedUser,
            @RequestParam(value = "longitude") double longitude,
            @RequestParam(value = "latitude") double latitude) {
        User user = service.setLocation(longitude, latitude, connectedUser);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/id-user")
    public ResponseEntity<User> getById(
            @Param(value = "id") Integer id, Principal connectedUser
    ){
        User user = service.getById(id, connectedUser);
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

    @GetMapping("/follower/{id}")
    public ResponseEntity<User> follower(Principal connectedUser, @PathVariable(value = "id")Integer id){
        return ResponseEntity.ok(service.followUser(connectedUser, id));
    }

    @DeleteMapping("/follower/{id}")
    public ResponseEntity<User> cancelFollower(Principal connectedUser, @PathVariable(value = "id")Integer id){
        return ResponseEntity.ok(service.cancelFollowUser(connectedUser, id));
    }

    @GetMapping("/get-followers/{id}")
    public ResponseEntity<List<User>> getFollowers(@PathVariable(value = "id") Integer id){
        List<User> users = service.getFollowers(id);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/get-watchers/{id}")
    public ResponseEntity<List<User>> getWatchers(@PathVariable(value = "id") Integer id){
        List<User> users = service.getWatchers(id);
        return ResponseEntity.ok(users);
    }
}
