package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.entity.chat.Conversation;
import com.thangkl2420.server_ducky.entity.chat.Message;
import com.thangkl2420.server_ducky.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
    @Query(value = "select m from Message m WHERE m.conversation.id = :id AND m.timestamp >= :startTime")
    Page<Message> findMessageById(Integer id, long startTime, Pageable pageable);

    @Query("SELECT c FROM Conversation c " +
            "JOIN UserConversation uc1 ON c.id = uc1.id.conversationId " +
            "JOIN UserConversation uc2 ON c.id = uc2.id.conversationId " +
            "WHERE uc1.id.userId = :userId1 AND uc2.id.userId = :userId2")
    List<Conversation> findConversationByUserIds(Integer userId1, Integer userId2);
    @Query("SELECT c FROM Conversation c " +
            "JOIN UserConversation uc ON c.id = uc.id.conversationId " +
            "WHERE uc.id.userId = :id")
    List<Conversation> findConversationByUser(Integer id);
    @Query("SELECT u FROM User u " +
            "JOIN UserConversation uc ON u.id = uc.id.userId " +
            "WHERE uc.id.conversationId = :id AND u.id != :idUser")
    Optional<User> findUserInConversation(Integer id, Integer idUser);

    @Query("SELECT m FROM Message m " +
            "WHERE m.conversation.id = :conversationId " +
            "ORDER BY m.timestamp DESC " +
            "LIMIT 1")
    Optional<Message> findLastMessageInConversation(Integer conversationId);
}
