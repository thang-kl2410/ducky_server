package com.thangkl2420.server_ducky.service;

import com.thangkl2420.server_ducky.dto.UserConversationId;
import com.thangkl2420.server_ducky.entity.Conversation;
import com.thangkl2420.server_ducky.entity.Message;
import com.thangkl2420.server_ducky.entity.UserConversation;
import com.thangkl2420.server_ducky.repository.ConversationRepository;
import com.thangkl2420.server_ducky.repository.MessageRepository;
import com.thangkl2420.server_ducky.repository.UserConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository repository;
    private final UserConversationRepository userConversationRepository;
    private final MessageRepository messageRepository;

    public List<Conversation> getAllConversation(Integer id){
        List<Conversation> conversations = userConversationRepository.findAllByUser(id);
        return  conversations;
    }

    public List<Message> getMessageByIdConversation(Integer id){
        return repository.findMessageById(id);
    }

    public Conversation getConversationByIds(Integer idUser1, Integer idUser2){
        List<Conversation> conversations = repository.findConversationByUserIds(idUser1, idUser2);
        if(conversations.isEmpty()){
            Conversation newConversation = repository.save(new Conversation());
            UserConversation uc = new UserConversation();
            uc.setId(new UserConversationId(newConversation.getId(), idUser1));
            userConversationRepository.save(uc);
            uc.setId(new UserConversationId(newConversation.getId(), idUser2));
            userConversationRepository.save(uc);
            return newConversation;
        } else {
            return conversations.get(0);
        }
    }



//    public Optional<String> getChatRoomId(
//            String senderId,
//            String recipientId,
//            boolean createNewRoomIfNotExists
//    ) {
//        return chatRoomRepository
//                .findBySenderIdAndRecipientId(senderId, recipientId)
//                .map(ChatRoom::getChatId)
//                .or(() -> {
//                    if(createNewRoomIfNotExists) {
//                        var chatId = createChatId(senderId, recipientId);
//                        return Optional.of(chatId);
//                    }
//
//                    return  Optional.empty();
//                });
//    }
//
//    private String createChatId(String senderId, String recipientId) {
//        var chatId = String.format("%s_%s", senderId, recipientId);
//
//        ChatRoom senderRecipient = ChatRoom
//                .builder()
//                .chatId(chatId)
//                .senderId(senderId)
//                .recipientId(recipientId)
//                .build();
//
//        ChatRoom recipientSender = ChatRoom
//                .builder()
//                .chatId(chatId)
//                .senderId(recipientId)
//                .recipientId(senderId)
//                .build();
//
//        chatRoomRepository.save(senderRecipient);
//        chatRoomRepository.save(recipientSender);
//
//        return chatId;
//    }
}
