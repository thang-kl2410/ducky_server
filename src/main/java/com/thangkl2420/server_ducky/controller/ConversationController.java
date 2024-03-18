package com.thangkl2420.server_ducky.controller;

import com.thangkl2420.server_ducky.dto.ConversationDto;
import com.thangkl2420.server_ducky.entity.Conversation;
import com.thangkl2420.server_ducky.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/conversation")
@RequiredArgsConstructor
public class ConversationController {
    private final ConversationService service;

    @GetMapping("/get-all")
    public ResponseEntity<List<ConversationDto>> getConversations(Principal connectedUser){
        return ResponseEntity.ok(service.getAllConversation(connectedUser));
    }
}
