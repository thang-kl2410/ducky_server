package com.thangkl2420.server_ducky.controller;

import com.thangkl2420.server_ducky.entity.Message;
import com.thangkl2420.server_ducky.entity.RescueType;
import com.thangkl2420.server_ducky.repository.RescueCallRepository;
import com.thangkl2420.server_ducky.repository.RescueRepository;
import com.thangkl2420.server_ducky.repository.RescueStateRepository;
import com.thangkl2420.server_ducky.repository.RescueTypeRepository;
import com.thangkl2420.server_ducky.service.RescueService;
import com.thangkl2420.server_ducky.service.SpecializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rescue")
@RequiredArgsConstructor
public class RescueController {
    private final RescueService service;
    private final SpecializationService specializationService;

    @GetMapping("/all-type")
    public ResponseEntity<List<RescueType>> getAllRescueType(){
        return ResponseEntity.ok(specializationService.getSpecialization());
    }

}
