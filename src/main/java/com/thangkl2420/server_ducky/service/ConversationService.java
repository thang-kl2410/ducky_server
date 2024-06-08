package com.thangkl2420.server_ducky.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.thangkl2420.server_ducky.dto.chat.ConversationDto;
import com.thangkl2420.server_ducky.dto.chat.UserConversationId;
import com.thangkl2420.server_ducky.entity.chat.Conversation;
import com.thangkl2420.server_ducky.entity.chat.Message;
import com.thangkl2420.server_ducky.entity.user.User;
import com.thangkl2420.server_ducky.entity.chat.UserConversation;
import com.thangkl2420.server_ducky.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository repository;
    private final UserConversationRepository userConversationRepository;
    private final MessageRepository messageRepository;
    private final NotificationService notificationService;

    private final BCryptPasswordEncoder encoder;

    public List<ConversationDto> getAllConversation(Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        List<Conversation> conversations = userConversationRepository.findAllByUser(user.getId());
        List<ConversationDto> cvs = conversations.stream()
                .map(conversation -> {
                    User otherUser = repository.findUserInConversation(conversation.getId(), user.getId()).orElse(null);
                    if (otherUser == null) {
                        return null;
                    }
                    Message lastMessage = repository.findLastMessageInConversation(conversation.getId()).orElse(null);
                    if(lastMessage != null){
                        return new ConversationDto(conversation.getId(), otherUser, lastMessage);
                    } else {
                        Message m = new Message(null,null,null,Long.valueOf(0),1,null, null);
                        return new ConversationDto(conversation.getId(), otherUser, m);
                    }
                })
                .filter(Objects::nonNull)
                .filter(conversationDto -> conversationDto.getLastMessage() != null)
                .collect(Collectors.toList());
        Collections.sort(cvs, Comparator.comparing(c -> c.getLastMessage().getTimestamp()));
        return cvs;
    }

    public List<Message> getMessageByIdConversation(Integer id, long startTime, Integer pageIndex, Integer pageSize){
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return repository.findMessageById(id, startTime, pageable).getContent();
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

    public void sendMessage(Integer id, Message message, Principal connectedUser){
        Conversation conversation = repository.findById(id).orElseThrow();
        message.setConversation(conversation);
        messageRepository.save(message);

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        User otherUser = repository.findUserInConversation(conversation.getId(), user.getId()).orElse(null);
        try {
            if (otherUser.getIdDevice() != null && !otherUser.getIdDevice().isEmpty()) {
                notificationService.sendFCMById(
                        otherUser.getId(),
                        user.getLastname(),
                        message.getMessageType().getId() == 1 ? message.getContent() : "Hình ảnh",
                        user.getId()
                );
            }
        } catch (FirebaseMessagingException e) {
            System.out.println(e);
        }
    }

    String encryptMessage(String message){
        return  encoder.encode(message);
    }
//
//    List<Message> decryptMessage(List<Message> messages){
//        for (Message message : messages) {
//            String decryptedContent = encoder.d(message.getContent());
//            // Hiển thị decryptedContent cho người dùng
//        }
//    }

}
