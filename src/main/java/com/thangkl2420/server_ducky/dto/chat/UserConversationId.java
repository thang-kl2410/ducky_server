package com.thangkl2420.server_ducky.dto.chat;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserConversationId implements Serializable {
    @Column(name = "conversation_id")
    private Integer conversationId;
    @Column(name = "user_id")
    private Integer userId;
}
