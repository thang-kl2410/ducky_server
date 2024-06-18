package com.thangkl2420.server_ducky.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.thangkl2420.server_ducky.dto.rescue.RescueCallDto;
import com.thangkl2420.server_ducky.dto.rescue.RescueCallResponseDto;
import com.thangkl2420.server_ducky.entity.rescue.RescueCall;
import com.thangkl2420.server_ducky.entity.rescue.RescueDetail;
import com.thangkl2420.server_ducky.entity.rescue.RescueType;
import com.thangkl2420.server_ducky.entity.user.User;
import com.thangkl2420.server_ducky.exception.ApiResponse;
import com.thangkl2420.server_ducky.exception.InternalServerErrorException;
import com.thangkl2420.server_ducky.service.NotificationService;
import com.thangkl2420.server_ducky.service.RescueService;
import com.thangkl2420.server_ducky.service.SpecializationService;
import com.thangkl2420.server_ducky.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/rescue")
@RequiredArgsConstructor
public class RescueController {
    private final RescueService service;
    private final SpecializationService specializationService;
    private final UserService userService;
    private final NotificationService notificationService;

    @GetMapping("/all-type")
    public ResponseEntity<List<RescueType>> getAllRescueType(){
        return ResponseEntity.ok(specializationService.getSpecialization());
    }

    @PostMapping("/create")
    public ResponseEntity<RescueCallResponseDto> createRescueCall(@RequestBody RescueCall rescueCall, Principal connectedUser){
        RescueCallResponseDto rc = service.createRescueCall(connectedUser, rescueCall);
        notificationService.sendNotificationToMultipleUsers(rescueCall, rc.getRescuers().stream().map(User::getIdDevice).collect(Collectors.toList()));
        return ResponseEntity.ok(rc);
    }

    @PostMapping("/set")
    public ResponseEntity<List<RescueType>> setRescueType(@RequestBody List<Integer> ids, Principal connectedUser){
        service.updateUserRescue(connectedUser, ids);
        return ResponseEntity.ok(service.getSpecializationByUser(connectedUser));
    }

    @GetMapping("/confirm-rescue")
    public ResponseEntity<?> confirmRescue(@Param(value = "id") Integer id,Principal connectedUser){
        service.confirmRescue(connectedUser, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rescue-user")
    public ResponseEntity<List<RescueType>> getAllRescueByUser(Principal connectedUser){
        return ResponseEntity.ok(service.getSpecializationByUser(connectedUser));
    }

    @GetMapping("/rescue-by-type/{id}")
    public ResponseEntity<List<RescueDetail>> getAllRescueByType(@PathVariable(value = "id") Integer id){
        return ResponseEntity.ok(service.getAllRescueByType(id));
    }

    @GetMapping("/user-by-type/{id}")
    public ResponseEntity<List<User>> getAllUserByType(@PathVariable(value = "id") Integer id){
        return ResponseEntity.ok(service.getAllUserByType(id));
    }

    @GetMapping("/confirm-participate/{id}")
    public ResponseEntity<Boolean> confirm(@PathVariable(value = "id") Integer id, @Param("distance") double distance, Principal connectedUser){
        return ResponseEntity.ok(service.confirmParticipate(id, connectedUser, distance));
    }

    @GetMapping("/finish-rescue/{id}")
    public ResponseEntity<User> finishRescue(@PathVariable(value = "id" ) Integer id, Principal connectedUser){
        User user = service.finishWaiting(id);
        if(user.getId() != null){
            try {
                notificationService.sendFCMFinishRescue(user.getIdDevice(), id, user.getId());
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException(e);
            }
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/complete-rescue/{id}")
    public ResponseEntity<Boolean> completeRescue(@PathVariable(value = "id") Integer id){
        User u = service.completeRescue(id);
        User rescuer = service.completeRescue2(id);
        try {
            notificationService.finishRescueCall(u);
            notificationService.finishRescueCall(rescuer);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(true);
    }

    @GetMapping("/rescue-call/{id}")
    public ResponseEntity<RescueCallDto> getRescueDetail(@PathVariable(value = "id") Integer id){
        return ResponseEntity.ok(service.getRescueCallDetailById(id));
    }

    @GetMapping("/rescue-call/current")
    public ResponseEntity<RescueCallDto> getCurrentRescueDetail(Principal connectedUser){
        return ResponseEntity.ok(service.getCurrentRescueCallDetail(connectedUser));
    }

    @GetMapping("/my-rescue-call/current")
    public ResponseEntity<List<RescueCall>> getMyCurrentRescueDetail(Principal connectedUser){
        return ResponseEntity.ok(service.getMyCurrentRescueCallDetail(connectedUser));
    }

}
