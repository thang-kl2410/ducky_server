package com.thangkl2420.server_ducky.dto.chat;

import com.thangkl2420.server_ducky.entity.chat.Message;
import com.thangkl2420.server_ducky.entity.user.User;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ConversationDto implements Comparable{
    private Integer conversationId;
    private User user;
    private Message lastMessage;

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
