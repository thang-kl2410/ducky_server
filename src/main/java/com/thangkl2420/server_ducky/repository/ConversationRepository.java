package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.entity.Conversation;
import com.thangkl2420.server_ducky.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
    @Query(value = "select c.messages from Conversation c where c.id = :id ")
    List<Message> findMessageById(Integer id);
    @Query("SELECT c FROM Conversation c " +
            "JOIN UserConversation uc1 ON c.id = uc1.id.conversationId " +
            "JOIN UserConversation uc2 ON c.id = uc2.id.conversationId " +
            "WHERE uc1.id.userId = :userId1 AND uc2.id.userId = :userId2")
    List<Conversation> findConversationByUserIds(Integer userId1, Integer userId2);
}
