package com.thangkl2420.server_ducky.entity.chat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room_video_call")
public class RoomVideoCall {
    @Id
    String id;
    boolean isExpiry;
    long timestamp;
    Integer creator;
    Integer receiver;
}
