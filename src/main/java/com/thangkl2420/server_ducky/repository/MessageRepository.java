package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.entity.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
