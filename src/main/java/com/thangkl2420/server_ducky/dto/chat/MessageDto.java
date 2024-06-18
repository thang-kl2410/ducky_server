package com.thangkl2420.server_ducky.dto.chat;

import com.thangkl2420.server_ducky.entity.chat.Message;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MessageDto {
    private Message message;
    private Boolean isDelete;
}
