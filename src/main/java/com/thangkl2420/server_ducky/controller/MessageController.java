package com.thangkl2420.server_ducky.controller;

import com.thangkl2420.server_ducky.entity.Conversation;
import com.thangkl2420.server_ducky.entity.Message;
import com.thangkl2420.server_ducky.service.ConversationService;
import com.thangkl2420.server_ducky.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class MessageController {
    private final ConversationService conversationService;
    private final MessageService messageService;

    @GetMapping("/get-conversation-by-ids")
    public ResponseEntity<Conversation> getConversationByIds(
            @Param(value = "senderId") Integer senderId, @Param(value = "receiverId") Integer receiverId
    ){
        if(senderId == null || receiverId == null){
            return null;
        }
        return ResponseEntity.ok(conversationService.getConversationByIds(senderId, receiverId));
    }

    @SendTo("/topic/{id}/greetings")
    @MessageMapping("/hello/{id}")
    public Message message(@DestinationVariable Integer id, Message message, Principal connectedUser) throws Exception {
        conversationService.sendMessage(id, message, connectedUser);
        Thread.sleep(500);

        return message;
    }

    @GetMapping("/conversation/id")
    public ResponseEntity<List<Message>> getMessagesByIdConversation(@Param(value = "id") Integer id){
        return ResponseEntity.ok(conversationService.getMessageByIdConversation(id));
    }

    @GetMapping("/video-call/{idUser}/{chanelId}/{token}/")
    public ResponseEntity<?> joinVideoCall(@Param(value = "idUser") Integer idUser, @Param(value = "chanelId") String chanelId, @Param(value = "token") String token){
        messageService.joinVideoCallChanel(idUser, token, chanelId);
        return ResponseEntity.ok().build();
    }
}
