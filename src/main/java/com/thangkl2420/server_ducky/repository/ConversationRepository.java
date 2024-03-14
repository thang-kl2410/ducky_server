package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.entity.Conversation;
import com.thangkl2420.server_ducky.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
    @Query(value = "select c.messages from Conversation c where c.id = :id ")
    List<Message> findMessageById(Integer id);
}
