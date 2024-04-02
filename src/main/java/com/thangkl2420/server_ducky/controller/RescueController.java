package com.thangkl2420.server_ducky.controller;

import com.thangkl2420.server_ducky.entity.Message;
import com.thangkl2420.server_ducky.entity.RescueCall;
import com.thangkl2420.server_ducky.entity.RescueType;
import com.thangkl2420.server_ducky.repository.RescueCallRepository;
import com.thangkl2420.server_ducky.repository.RescueRepository;
import com.thangkl2420.server_ducky.repository.RescueStateRepository;
import com.thangkl2420.server_ducky.repository.RescueTypeRepository;
import com.thangkl2420.server_ducky.service.NotificationService;
import com.thangkl2420.server_ducky.service.RescueService;
import com.thangkl2420.server_ducky.service.SpecializationService;
import com.thangkl2420.server_ducky.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
    public ResponseEntity<?> createRescueCall(@RequestBody RescueCall rescueCall, Principal connectedUser){
        service.createRescueCall(connectedUser, rescueCall);
        List<String> devices = userService.getAllTokenDeviceUserByRescue(connectedUser, rescueCall);
        notificationService.sendNotificationToMultipleUsers(rescueCall, devices);
        return ResponseEntity.ok().build();
    }
}
