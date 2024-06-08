package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.dto.chat.UserConversationId;
import com.thangkl2420.server_ducky.entity.chat.Conversation;
import com.thangkl2420.server_ducky.entity.chat.UserConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserConversationRepository extends JpaRepository<UserConversation, UserConversationId> {
    @Query(value = "select uc.conversation from UserConversation uc where uc.id.userId = :id ")
    List<Conversation> findAllByUser(Integer id);

}
