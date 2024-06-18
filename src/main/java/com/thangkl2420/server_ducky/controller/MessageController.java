package com.thangkl2420.server_ducky.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.thangkl2420.server_ducky.dto.chat.MessageDto;
import com.thangkl2420.server_ducky.entity.chat.Conversation;
import com.thangkl2420.server_ducky.entity.chat.Message;
import com.thangkl2420.server_ducky.entity.chat.RoomVideoCall;
import com.thangkl2420.server_ducky.service.ConversationService;
import com.thangkl2420.server_ducky.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class MessageController {
    private final ConversationService conversationService;
    private final NotificationService notificationService;

    @GetMapping("/get-conversation-by-ids")
    public ResponseEntity<Conversation> getConversationByIds(
            @Param(value = "senderId") Integer senderId, @Param(value = "receiverId") Integer receiverId
    ){
        if(senderId == null || receiverId == null){
            return null;
        }
        return ResponseEntity.ok(conversationService.getConversationByIds(senderId, receiverId));
    }

    @SendTo("/topic/{id}/chats")
    @MessageMapping("/chats/{id}")
    public MessageDto message(@DestinationVariable Integer id, MessageDto message, Principal connectedUser) throws Exception {
        conversationService.sendMessage(id, message, connectedUser);
        return message;
    }

    @GetMapping("/conversation/id")
    public ResponseEntity<List<Message>> getMessagesByIdConversation(
            @Param(value = "id") Integer id, @Param(value = "startTime") long startTime,
            @Param(value = "pageIndex") Integer pageIndex, @Param(value = "pageSize") Integer pageSize
    ){
        return ResponseEntity.ok(conversationService.getMessageByIdConversation(id, startTime, pageIndex, pageSize));
    }

    @PostMapping("/rtc/create/{receiver}")
    public ResponseEntity<String> createRoom(@PathVariable(value = "receiver") Integer receiver,@RequestBody String roomId, Principal connectedUser){
        try {
            conversationService.createRoom(roomId, receiver, connectedUser);
            notificationService.sendNotificationRtc(receiver, roomId,connectedUser);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(roomId);
    }

    @GetMapping("/rtc/{id}")
    public ResponseEntity<Boolean> createRoom(@PathVariable(value = "id") String id){
        return ResponseEntity.ok(conversationService.checkRoom(id));
    }

    @DeleteMapping("/rtc/end/{id}")
    public ResponseEntity<Boolean> endVideoCall(@PathVariable(value = "id") String id){
        try {
            RoomVideoCall rvc = conversationService.endVideoCall(id);
            if(rvc != null){
                notificationService.sendNotificationEndRtc(rvc);
            }
        } catch (FirebaseMessagingException e) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);
    }
}
