package com.thangkl2420.server_ducky.dto.chat;

import com.thangkl2420.server_ducky.entity.chat.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Message message;
    private Boolean isDelete;
}
