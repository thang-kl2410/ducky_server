package com.thangkl2420.server_ducky.dto;

import com.thangkl2420.server_ducky.entity.Conversation;
import com.thangkl2420.server_ducky.entity.Message;
import com.thangkl2420.server_ducky.entity.User;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDto implements Comparable{
    private Integer conversationId;
    private User user;
    private Message lastMessage;

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
