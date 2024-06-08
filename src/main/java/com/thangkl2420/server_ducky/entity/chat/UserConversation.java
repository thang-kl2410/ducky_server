package com.thangkl2420.server_ducky.entity.chat;

import com.thangkl2420.server_ducky.dto.chat.UserConversationId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thangkl2420.server_ducky.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_conversation")
public class UserConversation {
    @EmbeddedId
    private UserConversationId id;

    @ManyToOne
    @JoinColumn(name = "conversation_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Conversation conversation;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;
}
