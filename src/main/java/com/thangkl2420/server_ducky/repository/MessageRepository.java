package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
