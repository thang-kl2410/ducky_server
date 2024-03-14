package com.thangkl2420.server_ducky.service;

import com.thangkl2420.server_ducky.entity.User;
import com.thangkl2420.server_ducky.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final FCMService fcmService;
    private final UserRepository userRepository;
//    public ChatMessage save(ChatMessage chatMessage) {
//        var chatId = chatRoomService
//                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
//                .orElseThrow(); // You can create your own dedicated exception
//        chatMessage.setChatId(chatId);
//        repository.save(chatMessage);
//        return chatMessage;
//    }
//
//    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
//        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
//        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
//    }

    @SneakyThrows
    public void joinVideoCallChanel(Integer idUser, String token, String chanel){
        User user = userRepository.findById(idUser).orElseThrow();
        fcmService.sendFCMNotification(user.getIdDevice(), "Có cuộc gọi đến",chanel);
    }
}
