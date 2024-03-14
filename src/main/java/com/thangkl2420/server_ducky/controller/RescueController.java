package com.thangkl2420.server_ducky.controller;

import com.thangkl2420.server_ducky.repository.RescueCallRepository;
import com.thangkl2420.server_ducky.repository.RescueRepository;
import com.thangkl2420.server_ducky.repository.RescueStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rescue")
@RequiredArgsConstructor
public class RescueController {
    private final RescueRepository repository;
    private final RescueCallRepository rescueCallRepository;
    private final RescueStateRepository rescueStateRepository;


}
